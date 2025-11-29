package com.example.carespawbe.repository.shop;

import com.example.carespawbe.entity.shop.VoucherEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VoucherRepository extends JpaRepository<VoucherEntity, Long> {
    Optional<VoucherEntity> findByVoucherName(String voucherName);
    List<VoucherEntity> findAllByShop_ShopId(Long shopId);
    VoucherEntity findVoucherEntitiesByVoucherId(Long voucherId);

}
