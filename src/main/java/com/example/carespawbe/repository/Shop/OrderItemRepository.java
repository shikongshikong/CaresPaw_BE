package com.example.carespawbe.repository.Shop;

import com.example.carespawbe.dto.Shop.UserProductOrderTimeDTO;
import com.example.carespawbe.entity.Shop.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

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

    @Query("""
        select new com.example.carespawbe.dto.Shop.UserProductOrderTimeDTO(
            o.userEntity.id,
            p.productId,
            o.orderCreatedAt
        )
        from OrderItemEntity oi
        join oi.shopOrder so
        join so.order o
        join oi.productSku sku
        join sku.product p
        order by o.orderCreatedAt desc
    """)
    List<UserProductOrderTimeDTO> findUserProductOrderTimes();

    @Query("""
    select
      function('year', o.orderCreatedAt),
      function('month', o.orderCreatedAt),
      coalesce(sum(oi.orderItemTotalPrice), 0)
    from OrderItemEntity oi
    join oi.shopOrder so
    join so.order o
    where so.shop.shopId = :shopId
      and so.shopOrderStatus = :completedStatus
      and o.orderCreatedAt >= :start
      and o.orderCreatedAt < :end
    group by function('year', o.orderCreatedAt), function('month', o.orderCreatedAt)
    order by function('year', o.orderCreatedAt), function('month', o.orderCreatedAt)
""")
    List<Object[]> revenueByMonthForShopCompleted(
            @Param("shopId") Long shopId,
            @Param("completedStatus") int completedStatus,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );

    @Query("""
    select
      function('year', o.orderCreatedAt),
      function('month', o.orderCreatedAt),
      coalesce(sum(oi.orderItemQuantity), 0)
    from OrderItemEntity oi
    join oi.shopOrder so
    join so.order o
    where so.shop.shopId = :shopId
      and so.shopOrderStatus = :completedStatus
      and o.orderCreatedAt >= :start
      and o.orderCreatedAt < :end
    group by function('year', o.orderCreatedAt), function('month', o.orderCreatedAt)
    order by function('year', o.orderCreatedAt), function('month', o.orderCreatedAt)
""")
    List<Object[]> unitsByMonthForShopCompleted(
            @Param("shopId") Long shopId,
            @Param("completedStatus") int completedStatus,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );


}
