package com.example.carespawbe.repository.Shop;

import com.example.carespawbe.entity.Shop.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {

    Optional<CartItemEntity> findByCart_CartIdAndProductSku_ProductSkuId(Long cartId, Long skuId);

    List<CartItemEntity> findAllByCart_CartId(Long cartId);

    void deleteAllByCart_CartId(Long cartId);

    // checkout items thuộc user + nằm trong list id
    @Query("""
        select ci from CartItemEntity ci
        where ci.cart.userEntity.id = :userId
          and ci.cartItemId in :cartItemIds
    """)
    List<CartItemEntity> findCheckoutItems(@Param("userId") Long userId,
                                           @Param("cartItemIds") List<Long> cartItemIds);

    void deleteAllByCart_UserEntity_IdAndCartItemIdIn(Long userId, List<Long> cartItemIds);
}
