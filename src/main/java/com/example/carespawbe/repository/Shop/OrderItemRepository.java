package com.example.carespawbe.repository.Shop;

import com.example.carespawbe.entity.Shop.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {

    List<OrderItemEntity> findAllByShopOrder_ShopOrderId(Long shopOrderId);

    List<OrderItemEntity> findAllByShopOrder_Order_OrderId(Long orderId);
}
