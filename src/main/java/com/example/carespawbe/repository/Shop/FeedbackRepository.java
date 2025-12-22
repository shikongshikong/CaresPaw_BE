package com.example.carespawbe.repository.Shop;

import com.example.carespawbe.entity.Shop.FeedbackEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<FeedbackEntity, Long> {
    List<FeedbackEntity> findAllByOrderItem_Product_ProductId(Long productId);
    List<FeedbackEntity> findAllByUser_Id(Long userId);
}
