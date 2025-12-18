package com.example.carespawbe.repository.Shop;

import com.example.carespawbe.entity.Auth.UserEntity;
import com.example.carespawbe.entity.Shop.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    //tracking order
    List<OrderEntity> findAllByUserEntity_IdOrderByOrderCreatedAtDesc(Long userId);

    Optional<OrderEntity> findByOrderIdAndUserEntity_Id(Long orderId, Long userId);
    List<OrderEntity> findAllByUserEntity_Id(Long userId);
}
