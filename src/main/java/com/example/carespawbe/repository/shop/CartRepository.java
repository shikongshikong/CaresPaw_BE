package com.example.carespawbe.repository.shop;

import com.example.carespawbe.entity.shop.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface CartRepository extends JpaRepository<CartEntity, Long> {

//    Optional<CartEntity> findByUserUserId(Long userId);
}
