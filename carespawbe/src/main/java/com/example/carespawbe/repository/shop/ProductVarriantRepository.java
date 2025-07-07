package com.example.carespawbe.repository.shop;

import com.example.carespawbe.entity.shop.ProductEntity;
import com.example.carespawbe.entity.shop.ProductVarriantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductVarriantRepository extends JpaRepository<ProductVarriantEntity, Long> {
    void deleteByProductVarriants(ProductEntity product);

}