package com.example.carespawbe.repository.Shop;

import com.example.carespawbe.entity.Auth.UserEntity;
import com.example.carespawbe.entity.Shop.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    //tracking order
    List<OrderEntity> findAllByUserEntity_IdOrderByOrderCreatedAtDesc(Long userId);

    Optional<OrderEntity> findByOrderIdAndUserEntity_Id(Long orderId, Long userId);
    List<OrderEntity> findAllByUserEntity_Id(Long userId);

    // ✅ Orders Today
    Long countByOrderCreatedAt(LocalDate orderCreatedAt);

    // ✅ Revenue chỉ tính đơn COMPLETED
    @Query("SELECT COALESCE(SUM(o.orderTotalPrice), 0) FROM OrderEntity o WHERE o.orderStatus = :status")
    Double sumRevenueByStatus(@Param("status") int status);

    Long countByOrderCreatedAtBetween(LocalDate start, LocalDate end);

    @Query("""
    SELECT COALESCE(SUM(o.orderTotalPrice), 0)
    FROM OrderEntity o
    WHERE o.orderStatus = :status
      AND o.orderCreatedAt >= :start AND o.orderCreatedAt < :end
""")
    Double sumRevenueCompletedBetween(@Param("status") int status,
                                      @Param("start") LocalDate start,
                                      @Param("end") LocalDate end);

    @Modifying
    @Query("""
        update OrderEntity o
        set o.orderStatus = :status,
            o.orderUpdatedAt = :now
        where o.orderId = :orderId
    """)
    int updateOrderStatus(@Param("orderId") Long orderId,
                          @Param("status") Integer status,
                          @Param("now") LocalDate now);

}
