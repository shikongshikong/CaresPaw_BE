package com.example.carespawbe.repository.shop;

import com.example.carespawbe.entity.shop.ShopEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopRepository extends JpaRepository<ShopEntity, Long> {
}