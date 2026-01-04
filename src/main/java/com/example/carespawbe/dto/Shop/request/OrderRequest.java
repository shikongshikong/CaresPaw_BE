package com.example.carespawbe.dto.Shop.request;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {
    private Long shippingAddressId;
    private Double orderShippingFee;
    private List<ShopOrderRequest> shopOrders;
    private PaymentRequest payment;
    private Double orderTotalPrice;

}
