package com.example.carespawbe.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemRequest {
    private Long cartItemId;
    private Long cartId;
    private Double cartItemPrice;
    private Double cartItemOriginalPrice;
    private int cartItemQuantity;
    private Double cartItemTotalPrice;
    private boolean isFlashSale;
    private Long productId;
//    private Long productVariantId;
}
