package com.example.carespawbe.repository.Shop;

import com.example.carespawbe.entity.Shop.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, Long> {
      CartEntity findCartEntityByUserEntity_Id(Long id);
      CartEntity findCartEntityByCartId(Long cartId);

    Optional<CartEntity> findByUserEntity_Id(Long userId);
}
