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
    private ProductResponse product;
    private String productVarriantValue;
    private String imageProductUrl;
//    private String productName;
//    private Long productVarriantId;
//    private String variantName;
}
