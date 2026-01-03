package com.example.carespawbe.service.Expert;

import com.example.carespawbe.dto.Common.PagedResponse;
import com.example.carespawbe.dto.Expert.*;
import com.example.carespawbe.entity.Expert.ExpertEarningEntity;
import com.example.carespawbe.repository.Expert.ExpertEarningRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
public class ExpertCenterCoordination {

    @Autowired private AppointmentService appointmentService;
    @Autowired private ExpertEarningService earningService;
    @Autowired private ExpertEarningRepository earningRepository;

    // return a response
    public ExpertDashboardResponse getDashBoardStatisticItem(Long expertId){
        ExpertDashboardResponse response = new ExpertDashboardResponse();

        // get upcoming app information
        UpComingApp upComingApp = appointmentService.getUpComingApp(expertId);
        response.setUpComingApp(upComingApp);

        // get today apps list
        response.setTodayAppList(appointmentService.getTodayRemainingAppList(expertId));

        // get statistic infos: (total apps, total patients, total income, today remain apps)
        response.setTotalApps(appointmentService.getMonthlyAppointmentGrowth(expertId));
        response.setTotalPatients(appointmentService.getMonthlyPetGrowth(expertId));
        response.setTotalIncome(earningService.getMonthlyRevenueGrowth(expertId));
        response.setRemainApps(appointmentService.getRemainingCount(expertId));

        return null;
    }

    public PagedResponse<ExpertAppListItem> getAppList(Long expertId, ExpertAppListRequest request){
        return appointmentService.getAppointments(expertId, request.getPage(), request.getPageSize(), request.getKeyword(), request.getStatus(), request.getSort());
    }

    public void cancelApp(Long appId){
        appointmentService.cancelByPublicId(appId);
    }

    @Transactional()
    public ExpertEarningPageResponse getExpertEarnings(Long expertId, LocalDate from, LocalDate to, Integer status, String q,
                                                       int page1Based, int size, String sortKey, String sortDir) {

        Specification<ExpertEarningEntity> spec = Specification.allOf(
                ExpertEarningService.byExpertId(expertId),
                ExpertEarningService.createdFrom(from),
                ExpertEarningService.createdTo(to),
                ExpertEarningService.hasStatus(status),
                ExpertEarningService.searchQ(q)
        );

        Sort sort = buildSafeSort(sortKey, sortDir);

        int page0 = Math.max(0, page1Based - 1);
        int sizeSafe = Math.max(1, Math.min(size, 200)); // chặn size quá lớn
        Pageable pageable = PageRequest.of(page0, sizeSafe, sort);

        Page<ExpertEarningEntity> p = earningRepository.findAll(spec, pageable);

        List<ExpertEarningItemDto> items = p.getContent().stream()
                .map(this::toItemDto)
                .toList();

        ExpertEarningSummaryDto summary = earningService.calcSummary(spec);

        return new ExpertEarningPageResponse(items, page1Based, sizeSafe, p.getTotalElements(), summary);
    }

    private ExpertEarningItemDto toItemDto(ExpertEarningEntity e) {
        Long apptId = e.getAppointmentEntity() != null ? e.getAppointmentEntity().getId() : null;
        return new ExpertEarningItemDto(
                e.getId(),
                apptId,
                e.getTotalEarning(),
                e.getPlatformFee(),
                e.getExpertGain(),
                e.getStatus(),
                e.getCreateAt()
        );
    }

    /**
     * chống sort injection: chỉ cho sort theo whitelist field FE đang dùng
     */
    private Sort buildSafeSort(String sortKey, String sortDir) {
        Set<String> allowed = Set.of("id", "createAt", "totalEarning", "platformFee", "expertGain", "status");

        String key = (sortKey == null || sortKey.isBlank()) ? "createAt" : sortKey.trim();
        if (!allowed.contains(key)) key = "createAt";

        Sort.Direction dir = "asc".equalsIgnoreCase(sortDir) ? Sort.Direction.ASC : Sort.Direction.DESC;
        return Sort.by(dir, key);
    }

}