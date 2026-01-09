package com.example.carespawbe.repository.Shop;

import com.example.carespawbe.dto.Shop.response.ProductInfoDTO;
import com.example.carespawbe.entity.Shop.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    // ===== giữ nguyên của bạn =====
    List<ProductEntity> findTop6ByOrderByProductCreatedAtDesc();
    List<ProductEntity> findTop12ByOrderBySoldDesc();
    ProductEntity findProductEntityByProductId(Long productId);
    List<ProductEntity> findAllByShop_ShopIdOrderByProductCreatedAtDesc(Long shopId);
    List<ProductEntity> findAllByShop_ShopIdAndCategory_CategoryIdOrderByProductCreatedAtDesc(Long shopId, Long categoryId);

    @Query("SELECT p FROM ProductEntity p WHERE p.category.categoryId = :categoryId ORDER BY p.productCreatedAt DESC")
    List<ProductEntity> findProductsByCategoryId(@Param("categoryId") Long categoryId);

    @Query("""
        SELECT DISTINCT p
        FROM ProductEntity p
        LEFT JOIN FETCH p.skuList s
        WHERE p.productId = :id
    """)
    Optional<ProductEntity> findWithSkus(@Param("id") Long id);

    @Query(value = """
    SELECT p.*
    FROM product p
    JOIN (
        SELECT TOP (:limit)
               p2.product_id AS pid,
               COALESCE(SUM(CASE WHEN s.is_active = 1 THEN s.sold ELSE 0 END), 0) AS soldSum
        FROM product p2
        LEFT JOIN product_sku s ON s.product_id = p2.product_id
        WHERE p2.product_status = 1
        GROUP BY p2.product_id
        ORDER BY soldSum DESC, p2.product_id DESC
    ) t ON t.pid = p.product_id
    ORDER BY t.soldSum DESC, p.product_id DESC
""", nativeQuery = true)
    List<ProductEntity> findBestSellers(@Param("limit") int limit);

    // ✅ SYNC product.sold = SUM(sku.sold active) (SQL Server)
    @Modifying
    @Query(value = """
        UPDATE product
        SET sold = (
            SELECT COALESCE(SUM(COALESCE(s.sold,0)),0)
            FROM product_sku s
            WHERE s.product_id = :productId AND s.is_active = 1
        )
        WHERE product_id = :productId
    """, nativeQuery = true)
    void syncProductSoldFromSkus(@Param("productId") Long productId);

    @Query("""
        select new com.example.carespawbe.dto.Shop.response.ProductInfoDTO(
            p.productId,
            p.productName,
            p.productUsing,
            p.productCreatedAt,
            p.category.categoryId,
            p.shop.shopId
        )
        from ProductEntity p
        order by p.productCreatedAt desc
    """)
    List<ProductInfoDTO> findAllProductInfos();

    @Query("""
    SELECT p
    FROM ProductEntity p
    JOIN FETCH p.shop s
    JOIN FETCH p.category c
    WHERE
        (:kw IS NULL OR :kw = '' OR
         LOWER(p.productName) LIKE LOWER(CONCAT('%', :kw, '%')) OR
         LOWER(p.productUsing) LIKE LOWER(CONCAT('%', :kw, '%')) OR
         LOWER(s.shopName) LIKE LOWER(CONCAT('%', :kw, '%')) OR
         LOWER(c.categoryName) LIKE LOWER(CONCAT('%', :kw, '%'))
        )
    ORDER BY p.productCreatedAt DESC
""")
    List<ProductEntity> searchEntities(@Param("kw") String keyword);
}

