package com.example.carespawbe.dto.Shop.response;

import com.example.carespawbe.entity.Shop.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShopOrderResponse {
    private Long shopOrderId;
    private Double shippingFee;
    private int shopOrderStatus;
//    private String ghnOrderCode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long orderId;
    private Long shopId;

    private VoucherResponse orderVoucher;
    private VoucherResponse shippingVoucher;

    private List<OrderItemResponse> orderItemEntities;
//    private List<ShopOrderStatusHistoryEntity> shopOrderStatusHistories;

}
