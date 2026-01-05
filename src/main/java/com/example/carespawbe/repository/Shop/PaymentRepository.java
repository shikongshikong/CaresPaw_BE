package com.example.carespawbe.repository.Shop;

import com.example.carespawbe.entity.Shop.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
}
