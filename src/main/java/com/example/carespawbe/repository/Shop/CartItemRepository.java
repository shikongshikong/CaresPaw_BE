package com.example.carespawbe.repository.Shop;

import com.example.carespawbe.entity.Shop.CartEntity;
import com.example.carespawbe.entity.Shop.CartItemEntity;
import com.example.carespawbe.entity.Shop.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {
    CartItemEntity findByCartAndProductAndCartItemId(CartEntity cart, ProductEntity product, Long cartItemId);
    // Lấy danh sách CartItem theo CartId
//    List<CartItemEntity> findByCart_CartId(Long cartId);
}