package com.example.carespawbe.repository.Shop;

import com.example.carespawbe.entity.Shop.ImageProductEntity;
import com.example.carespawbe.entity.Shop.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageProductRepository  extends JpaRepository<ImageProductEntity, Long> {
    List<ImageProductEntity> findByImageProduct(ProductEntity product);
    List<ImageProductEntity> findByImageProduct_ProductId(Long productId);
    void deleteByImageProduct(ProductEntity product);

}
