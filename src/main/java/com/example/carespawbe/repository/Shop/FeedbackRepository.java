package com.example.carespawbe.repository.Shop;

import com.example.carespawbe.entity.Shop.FeedbackEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<FeedbackEntity, Long> {

    List<FeedbackEntity> findAllByOrderItem_ProductSku_Product_ProductId(Long productId);

    List<FeedbackEntity> findAllByUser_Id(Long userId);

    @Query("""
        SELECT COALESCE(AVG(f.star), 0.0)
        FROM FeedbackEntity f
        WHERE f.orderItem.productSku.product.productId = :productId
    """)
    Double getAverageRatingByProductId(@Param("productId") Long productId);
}

