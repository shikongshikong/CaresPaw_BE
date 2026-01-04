package com.example.carespawbe.dto.Shop.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemRequest {
    private Long cartItemId;
    private Long cartId;

    // ✅ BẮT BUỘC: SKU
    private Long productSkuId;

    private Integer cartItemQuantity;
}
