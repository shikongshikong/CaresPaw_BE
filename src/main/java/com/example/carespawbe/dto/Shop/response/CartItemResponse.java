package com.example.carespawbe.dto.Shop.response;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResponse {
    private Long cartItemId;

    // ✅ SKU
    private Long productSkuId;
    private String skuCode;
    private String variantText;

    private Integer cartItemQuantity;
    private Double cartItemPrice;        // snapshot (from SKU)
    private Double cartItemTotalPrice;

    private ProductResponse product;     // có thể giữ để show tên/ảnh
}
