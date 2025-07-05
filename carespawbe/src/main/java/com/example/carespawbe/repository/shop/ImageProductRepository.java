package com.example.carespawbe.repository.shop;

import com.example.carespawbe.entity.shop.ImageProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageProductRepository  extends JpaRepository<ImageProductEntity, Long> {
    List<ImageProductEntity> findByImageProduct_ProductId(Long productId);
}
