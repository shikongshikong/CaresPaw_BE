package com.example.carespawbe.dto.Shop.request;

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
    private Integer cartItemQuantity;
    private Double cartItemTotalPrice;
    private boolean isFlashSale;
    private Long productId;
//    private Long productVarriantId;
}