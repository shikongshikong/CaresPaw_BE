package com.example.carespawbe.repository.Shop;

import com.example.carespawbe.entity.Shop.OrderEntity;
import com.example.carespawbe.entity.Shop.ShopOrderEntity;
import org.hibernate.query.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShopOrderRepository extends JpaRepository<ShopOrderEntity, Long> {
    List<ShopOrderEntity> findAllByOrder_OrderId(Long orderId);

    List<ShopOrderEntity> findAllByOrder_UserEntity_IdOrderByCreatedAtDesc(Long userId);

    List<ShopOrderEntity> findAllByShop_ShopIdOrderByCreatedAtDesc(Long shopId);
    // Tìm đơn hàng theo Shop ID, có phân trang (Pagination)

    // Tìm đơn hàng theo Shop ID và Trạng thái (để lọc đơn hàng)
    List<ShopOrderEntity> findByShop_ShopIdAndShopOrderStatus(Long shopId, int status, Pageable pageable);
    @Query("SELECT so FROM ShopOrderEntity so " +
            "WHERE so.order.userEntity.id = :userId " +
            "ORDER BY so.createdAt DESC")
    List<ShopOrderEntity> findListShopOrderByUserId(@Param("userId") Long userId);
}
