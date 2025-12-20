package com.example.carespawbe.repository.Shop;

import com.example.carespawbe.entity.Shop.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> findTop6ByOrderByProductCreatedAtDesc();
    ProductEntity findProductEntityByProductId(Long productId);
    List<ProductEntity> findAllByShop_ShopIdOrderByProductCreatedAtDesc(Long shopId);
    List<ProductEntity> findAllByShop_ShopIdAndCategory_CategoryIdOrderByProductCreatedAtDesc(Long shopId, Long categoryId);

    @Query("SELECT p FROM ProductEntity p WHERE p.category.categoryId = :categoryId ORDER BY p.productCreatedAt DESC")
    List<ProductEntity> findProductsByCategoryId(@Param("categoryId") Long categoryId);
}
