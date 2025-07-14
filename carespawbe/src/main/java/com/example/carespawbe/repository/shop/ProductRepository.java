package com.example.carespawbe.repository.shop;

import com.example.carespawbe.entity.shop.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> findTop6ByOrderByProductCreatedAtDesc();

}
