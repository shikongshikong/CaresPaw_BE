package com.example.carespawbe.repository.Shop;

import com.example.carespawbe.entity.Shop.ShopEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShopRepository extends JpaRepository<ShopEntity, Long> {
//    Optional<ShopEntity> findByUserId(Long userId);
    Optional<ShopEntity> findByUser_Id(Long userId);

}