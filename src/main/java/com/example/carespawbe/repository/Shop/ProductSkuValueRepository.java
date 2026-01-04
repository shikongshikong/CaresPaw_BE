package com.example.carespawbe.repository.Shop;

import com.example.carespawbe.entity.Shop.ProductSkuValueEntity;
import com.example.carespawbe.entity.Shop.ProductSkuValueId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductSkuValueRepository extends JpaRepository<ProductSkuValueEntity, ProductSkuValueId> {

    List<ProductSkuValueEntity> findByProductSku_ProductSkuId(Long skuId);

    void deleteByProductSku_ProductSkuId(Long skuId);
    @Query("""
        SELECT sv
        FROM ProductSkuValueEntity sv
        JOIN FETCH sv.varriant v
        JOIN FETCH sv.varriantValue vv
        JOIN sv.productSku s
        JOIN s.product p
        WHERE p.productId = :productId
    """)
    List<ProductSkuValueEntity> findAllByProductIdFetchVariant(@Param("productId") Long productId);

}
