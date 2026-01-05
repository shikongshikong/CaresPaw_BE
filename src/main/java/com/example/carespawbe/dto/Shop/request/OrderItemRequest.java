package com.example.carespawbe.dto.Shop.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemRequest {
    private Long cartItemId;
    private Integer orderItemQuantity;
}
