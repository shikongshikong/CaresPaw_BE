package com.example.carespawbe.repository.Shop;

import com.example.carespawbe.entity.Shop.CartEntity;
import com.example.carespawbe.entity.Shop.CartItemEntity;
import com.example.carespawbe.entity.Shop.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {
    CartItemEntity findByCartAndProductAndCartItemId(CartEntity cart, ProductEntity product, Long cartItemId);

    //lấy danh sách cartItem theo user + list ids (dùng cho checkout)
    List<CartItemEntity> findAllByCart_UserEntity_IdAndCartItemIdIn(Long userId, List<Long> cartItemIds);

    //lấy 1 cartItem theo user + id (dùng cho validate 1 item)
    Optional<CartItemEntity> findByCartItemIdAndCart_UserEntity_Id(Long cartItemId, Long userId);

    //xoá nhiều cartItem theo user + ids (sau khi checkout xong)
    void deleteAllByCart_UserEntity_IdAndCartItemIdIn(Long userId, List<Long> cartItemIds);

    //nếu muốn fetch join để tránh Lazy load N+1
    @Query("""
        select ci
        from CartItemEntity ci
        join fetch ci.product p
        join fetch p.shop s
        where ci.cart.userEntity.id = :userId
          and ci.cartItemId in :ids
    """)
    List<CartItemEntity> findCheckoutItems(@Param("userId") Long userId, @Param("ids") List<Long> ids);
}