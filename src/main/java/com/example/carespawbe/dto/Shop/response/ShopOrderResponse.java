package com.example.carespawbe.dto.Shop.response;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShopOrderResponse {
    private Long shopOrderId;

    private Long shopId;
    private Integer shopOrderStatus;

    private Integer ghnServiceId;
    private Double shippingFee;

    private Long orderVoucherId;
    private Long shippingVoucherId;

    private List<OrderItemResponse> orderItems;
}
