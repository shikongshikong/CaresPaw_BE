package com.example.carespawbe.service.Expert;

import com.example.carespawbe.dto.Expert.DashBoardStatisticItem;
import com.example.carespawbe.dto.Expert.ExpertEarningSummaryDto;
import com.example.carespawbe.entity.Expert.ExpertEarningEntity;
import com.example.carespawbe.repository.Expert.ExpertEarningRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Optional;

@Service
public class ExpertEarningService {
    @Autowired
    private ExpertEarningRepository earningRepository;

    // Thay đổi kiểu trả về thành DashBoardStatisticItem<BigDecimal>
    public DashBoardStatisticItem<BigDecimal> getMonthlyRevenueGrowth(Long expertId) {
        // ... (phần xác định mốc thời gian giữ nguyên như trước) ...
        YearMonth currentMonth = YearMonth.now();
        // ... (khởi tạo startCurrent, endCurrent, startLast, endLast) ...
        LocalDateTime startCurrent = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime endCurrent = currentMonth.atEndOfMonth().atTime(23, 59, 59);

        LocalDateTime startLast = currentMonth.minusMonths(1).atDay(1).atStartOfDay();
        LocalDateTime endLast = currentMonth.minusMonths(1).atEndOfMonth().atTime(23, 59, 59);

        // Lấy tổng thu nhập
        BigDecimal revenueThisMonth = Optional.ofNullable(
                earningRepository.sumEarningsBetween(expertId, startCurrent, endCurrent)).orElse(BigDecimal.ZERO);

        BigDecimal revenueLastMonth = Optional.ofNullable(
                earningRepository.sumEarningsBetween(expertId, startLast, endLast)).orElse(BigDecimal.ZERO);

        // Tính phần trăm tăng trưởng
        double growthRate = 0.0;
        if (revenueLastMonth.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal difference = revenueThisMonth.subtract(revenueLastMonth);
            growthRate = difference.divide(revenueLastMonth, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .doubleValue();
        } else if (revenueThisMonth.compareTo(BigDecimal.ZERO) > 0) {
            growthRate = 100.0;
        }

        // Trả về đối tượng Generic (dùng Factory Method cho ngắn gọn)
        return DashBoardStatisticItem.of(revenueThisMonth, Math.round(growthRate * 100.0) / 100.0);
    }

    public static Specification<ExpertEarningEntity> byExpertId(Long expertId) {
        return (root, query, cb) -> cb.equal(root.get("id"), expertId);
//        return (root, query, cb) -> {
//            if (expertId == null) return cb.conjunction();
            // Phải truy cập vào trường expertEntity bên trong ExpertEarningEntity, sau đó mới lấy id
//            return cb.equal(root.get("expert").get("id"), expertId);
//        };
    }

    public static Specification<ExpertEarningEntity> createdFrom(LocalDate from) {
        return (root, query, cb) -> {
            if (from == null) return cb.conjunction();
            return cb.greaterThanOrEqualTo(root.get("createAt"), from.atStartOfDay());
        };
    }

    public static Specification<ExpertEarningEntity> createdTo(LocalDate to) {
        return (root, query, cb) -> {
            if (to == null) return cb.conjunction();
            LocalDateTime endExclusive = to.plusDays(1).atStartOfDay(); // inclusive end date
            return cb.lessThan(root.get("createAt"), endExclusive);
        };
    }

    public static Specification<ExpertEarningEntity> hasStatus(Integer status) {
        return (root, query, cb) -> status == null ? cb.conjunction() : cb.equal(root.get("status"), status);
    }

    /**
     * q: search theo earning_id hoặc app_id (FE placeholder: earningId, appointmentId…)
     */
    public static Specification<ExpertEarningEntity> searchQ(String q) {
        return (root, query, cb) -> {
            if (q == null || q.trim().isEmpty()) return cb.conjunction();
            String s = q.trim();

            Long val;
            try { val = Long.parseLong(s); } catch (Exception e) { return cb.conjunction(); }

            var appt = root.join("appointment", JoinType.LEFT);
            return cb.or(
                    cb.equal(root.get("id"), val),
                    cb.equal(appt.get("id"), val)
            );
        };
    }

    @PersistenceContext
    private EntityManager em;

    public ExpertEarningSummaryDto calcSummary(Specification<ExpertEarningEntity> spec) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root<ExpertEarningEntity> root = cq.from(ExpertEarningEntity.class);

        Predicate predicate = spec == null ? cb.conjunction() : spec.toPredicate(root, cq, cb);

        // SUM fields
        Expression<BigDecimal> sumTotal = cb.coalesce(cb.sum(root.get("totalEarning")), BigDecimal.ZERO);
        Expression<BigDecimal> sumFee = cb.coalesce(cb.sum(root.get("platformFee")), BigDecimal.ZERO);
        Expression<BigDecimal> sumNet = cb.coalesce(cb.sum(root.get("expertGain")), BigDecimal.ZERO);

        // unpaid: status == 0
        Expression<BigDecimal> unpaidAmount = cb.coalesce(
                cb.sum(
                        cb.<BigDecimal>selectCase()
                                .when(cb.equal(root.get("status"), 0), root.get("expertGain"))
                                .otherwise(BigDecimal.ZERO)
                ),
                BigDecimal.ZERO
        );

        Expression<Long> unpaidCount = cb.coalesce(
                cb.sum(
                        cb.<Long>selectCase()
                                .when(cb.equal(root.get("status"), 0), 1L)
                                .otherwise(0L)
                ),
                0L
        );

        cq.multiselect(
                sumTotal.alias("total"),
                sumFee.alias("fee"),
                sumNet.alias("net"),
                unpaidAmount.alias("unpaidAmount"),
                unpaidCount.alias("unpaidCount")
        );

        if (predicate != null) cq.where(predicate);

        Tuple t = em.createQuery(cq).getSingleResult();

        return new ExpertEarningSummaryDto(
                (BigDecimal) t.get("total"),
                (BigDecimal) t.get("fee"),
                (BigDecimal) t.get("net"),
                (BigDecimal) t.get("unpaidAmount"),
                (Long) t.get("unpaidCount")
        );
    }
}
