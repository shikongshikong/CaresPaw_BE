package com.example.carespawbe.repository.Shop;

import com.example.carespawbe.entity.Shop.VoucherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface VoucherRepository extends JpaRepository<VoucherEntity, Long> {

    List<VoucherEntity> findAllByShop_ShopId(Long shopId);

    Optional<VoucherEntity> findByShop_ShopIdAndVoucherNameIgnoreCase(Long shopId, String voucherName);

    // Danh sách voucher "available" cho shop tại ngày checkout (dùng ở trang order)
    @Query("""
        select v from VoucherEntity v
        where v.shop.shopId = :shopId
          and v.voucherAmount > 0
          and v.startedAt <= :today
          and v.finishedAt >= :today
        order by v.startedAt desc, v.voucherId desc
    """)
    List<VoucherEntity> findAvailableByShop(Long shopId, LocalDate today);

    // Khóa record khi trừ số lượng voucher (tránh đặt hàng đồng thời bị âm)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<VoucherEntity> findByVoucherId(Long voucherId);

    Optional<VoucherEntity> findByVoucherIdAndShop_ShopId(Long voucherId, Long shopId);
}
