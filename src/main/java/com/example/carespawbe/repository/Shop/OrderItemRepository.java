package com.example.carespawbe.repository.Shop;

import com.example.carespawbe.entity.Shop.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {

    // ✅ SOLD theo SKU
    @Query("""
        SELECT COALESCE(SUM(oi.orderItemQuantity), 0)
        FROM OrderItemEntity oi
        JOIN oi.shopOrder so
        WHERE oi.productSku.productSkuId = :skuId
          AND so.shopOrderStatus = :status
    """)
    Long countTotalSoldBySkuId(@Param("skuId") Long skuId, @Param("status") int status);

    // ✅ (optional) tổng sold theo PRODUCT (join qua SKU)
    @Query("""
        SELECT COALESCE(SUM(oi.orderItemQuantity), 0)
        FROM OrderItemEntity oi
        JOIN oi.shopOrder so
        WHERE oi.productSku.product.productId = :productId
          AND so.shopOrderStatus = :status
    """)
    Long countTotalSoldByProductId(@Param("productId") Long productId, @Param("status") int status);
}
