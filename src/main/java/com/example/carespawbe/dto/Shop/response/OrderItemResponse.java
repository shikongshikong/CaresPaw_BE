package com.example.carespawbe.dto.Shop.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemResponse {
    private Long orderItemId;

    private Integer orderItemQuantity;
    private Double orderItemPrice;
    private Double orderItemTotalPrice;

//    private Long productVarriantId;
    private Long productId;
    private Long shopOrderId;
}
