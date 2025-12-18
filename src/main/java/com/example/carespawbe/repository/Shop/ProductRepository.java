package com.example.carespawbe.repository.Shop;

import com.example.carespawbe.entity.Shop.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> findTop6ByOrderByProductCreatedAtDesc();
    ProductEntity findProductEntityByProductId(Long productId);
    List<ProductEntity> findAllByShop_ShopIdOrderByProductCreatedAtDesc(Long shopId);
}
