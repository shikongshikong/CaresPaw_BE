package com.example.carespawbe.repository.shop;

import com.example.carespawbe.entity.shop.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {

    // Lấy danh sách CartItem theo CartId
//    List<CartItemEntity> findByCart_CartId(Long cartId);
}
