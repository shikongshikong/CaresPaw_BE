package com.example.carespawbe.repository.Shop;

import com.example.carespawbe.entity.Shop.ShopEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ShopRepository extends JpaRepository<ShopEntity, Long> {
//    Optional<ShopEntity> findByUserId(Long userId);
    Optional<ShopEntity> findByUser_Id(Long userId);
    Optional<Long> findShopIdByUserId( Long userId);
}