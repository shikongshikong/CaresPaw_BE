package com.example.carespawbe.dto.Shop.request;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {
    // FK -> user_address.address_id
    private Long shippingAddressId;

    // orders.order_shipping_fee
    private Double orderShippingFee;

    // orders.order_total_price (FE có thể gửi, BE sẽ tính lại)
    private Double orderTotalPrice;

    // orders.order_status
    private Integer orderStatus;

    // children tables
    private List<ShopOrderRequest> shopOrders;
    private PaymentRequest payment;
}
