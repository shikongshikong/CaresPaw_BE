package com.example.carespawbe.repository.shop;

import com.example.carespawbe.entity.shop.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
}
