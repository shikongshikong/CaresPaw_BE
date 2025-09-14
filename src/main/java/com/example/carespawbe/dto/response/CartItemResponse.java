package com.example.carespawbe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResponse {
    private Long cartItemId;
    private Double cartItemPrice;
    private Double cartItemOriginalPrice;
    private int cartItemQuantity;
    private Double cartItemTotalPrice;
    private boolean isFlashSale;
    private Long productId;
//    private String productName;
    private Long productVariantId;
//    private String variantName;
}
