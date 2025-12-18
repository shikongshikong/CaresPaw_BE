package com.example.carespawbe.dto.Shop.request;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShopOrderRequest {
    // FK -> shop.shop_id
    private Long shopId;

    // shop_orders.shop_order_status
    private Integer shopOrderStatus;

    // GHN
//    private Integer ghnServiceId;
    private Double shippingFee;

    // FK -> voucher.voucher_id (nullable)
    private Long orderVoucherId;    // voucherUsageType=2
    private Long shippingVoucherId; // voucherUsageType=1

    // children table
    private List<OrderItemRequest> orderItems;
}
