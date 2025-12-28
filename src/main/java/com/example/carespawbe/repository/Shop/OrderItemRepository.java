package com.example.carespawbe.repository.Shop;

import com.example.carespawbe.entity.Shop.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {

    List<OrderItemEntity> findAllByShopOrder_ShopOrderId(Long shopOrderId);

    List<OrderItemEntity> findAllByShopOrder_Order_OrderId(Long orderId);

    @Query("SELECT COALESCE(SUM(oi.orderItemQuantity), 0) " +
            "FROM OrderItemEntity oi " +       // Tên class
            "JOIN oi.shopOrder so " +          // Biến 'shopOrder' dòng 40
            "WHERE oi.product.productId = :productId " + // Biến 'product' dòng 35
            "AND so.shopOrderStatus = :status") // Check bên ShopOrderEntity tên biến status là gì (thường là shopOrderStatus)
    Long countTotalSoldByProductId(@Param("productId") Long productId, @Param("status") int status);
}
