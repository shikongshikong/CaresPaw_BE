package com.example.carespawbe.repository.Shop;

import com.example.carespawbe.entity.Shop.OrderEntity;
import com.example.carespawbe.entity.Shop.ShopOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShopOrderRepository extends JpaRepository<ShopOrderEntity, Long> {
    List<ShopOrderEntity> findAllByOrder_OrderId(Long orderId);

    List<ShopOrderEntity> findAllByOrder_UserEntity_IdOrderByCreatedAtDesc(Long userId);

    List<ShopOrderEntity> findAllByShop_ShopIdOrderByCreatedAtDesc(Long shopId);
}
