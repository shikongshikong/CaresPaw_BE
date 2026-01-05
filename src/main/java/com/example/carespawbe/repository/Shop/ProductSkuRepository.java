package com.example.carespawbe.repository.Shop;

import com.example.carespawbe.entity.Shop.ProductSkuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductSkuRepository extends JpaRepository<ProductSkuEntity, Long> {

    List<ProductSkuEntity> findByProduct_ProductIdOrderByProductSkuIdAsc(Long productId);

    // ✅ FE reload sẽ không thấy SKU đã soft-delete
    List<ProductSkuEntity> findByProduct_ProductIdAndIsActiveTrueOrderByProductSkuIdAsc(Long productId);

    boolean existsBySkuCode(String skuCode);

    List<ProductSkuEntity> findAllByProductSkuIdIn(List<Long> ids);

    boolean existsByProduct_ProductIdAndSkuCode(Long productId, String skuCode);

    Optional<ProductSkuEntity> findByProduct_ProductIdAndSkuCode(Long productId, String skuCode);

    long countByProduct_ProductId(Long productId);

    // ✅ SUM stock của SKU active => Amount của product
    @Query("SELECT COALESCE(SUM(s.stock), 0) FROM ProductSkuEntity s WHERE s.product.productId = :productId AND s.isActive = true")
    Integer sumActiveStockByProductId(@Param("productId") Long productId);

    // ✅ SUM sold của SKU active theo productId
    @Query("SELECT COALESCE(SUM(COALESCE(s.sold,0)),0) FROM ProductSkuEntity s WHERE s.product.productId = :productId AND s.isActive = true")
    Long sumActiveSoldByProductId(@Param("productId") Long productId);

    // ✅ Cộng sold cho 1 SKU (atomic, tránh race condition)
    @Modifying
    @Query("""
        UPDATE ProductSkuEntity s
        SET s.sold = COALESCE(s.sold, 0) + :qty
        WHERE s.productSkuId = :skuId
    """)
    int increaseSold(@Param("skuId") Long skuId, @Param("qty") long qty);
}
