package com.example.carespawbe.entity.Shop;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cart_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    private Long cartItemId;

    @Column(name = "cart_item_price", nullable = false)
    private Double cartItemPrice = 0.0;  // snapshot price from SKU

    @Column(name = "cart_item_quantity", nullable = false)
    private Integer cartItemQuantity = 1;

    @Column(name = "cart_item_total_price", nullable = false)
    private Double cartItemTotalPrice = 0.0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private CartEntity cart;

    // ✅ chuẩn 5 bảng: cart item trỏ SKU
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_sku_id", nullable = false)
    private ProductSkuEntity productSku;

    // ✅ snapshot text để hiển thị nhanh (optional)
    @Column(name = "variant_text", columnDefinition = "NVARCHAR(255)")
    private String variantText;

    // ✅ snapshot sku code (optional)
    @Column(name = "sku_code", columnDefinition = "NVARCHAR(80)")
    private String skuCode;
}


