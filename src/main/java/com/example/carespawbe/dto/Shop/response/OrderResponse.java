package com.example.carespawbe.dto.Shop.response;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {
    private Long orderId;

    private Double orderShippingFee;
    private Double orderTotalPrice;
    private Integer orderStatus;
    private LocalDate orderCreatedAt;

    private Long shippingAddressId;

    private List<ShopOrderResponse> shopOrders;
}
