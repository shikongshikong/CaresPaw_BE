package com.example.carespawbe.repository.shop;

import com.example.carespawbe.entity.shop.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<CartEntity, Long> {

//    Optional<CartEntity> findByUserUserId(Long userId);
}
