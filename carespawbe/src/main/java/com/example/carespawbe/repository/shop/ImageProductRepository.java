package com.example.carespawbe.repository.shop;

import com.example.carespawbe.entity.shop.ImageProductEntity;
import com.example.carespawbe.entity.shop.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageProductRepository  extends JpaRepository<ImageProductEntity, Long> {
    List<ImageProductEntity> findByImageProduct(ProductEntity product);
    void deleteByImageProduct(ProductEntity product);

}
