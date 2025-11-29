package com.example.carespawbe.repository.shop;

import com.example.carespawbe.entity.shop.ProductEntity;
import com.example.carespawbe.entity.shop.ProductVarriantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductVarriantRepository extends JpaRepository<ProductVarriantEntity, Long> {
    void deleteByProductVarriants(ProductEntity product);
    ProductVarriantEntity findByProductVarriantId(Long productVarriantId);
    List<ProductVarriantEntity> findProductVarriantEntitiesByProductVarriants(ProductEntity product);

}