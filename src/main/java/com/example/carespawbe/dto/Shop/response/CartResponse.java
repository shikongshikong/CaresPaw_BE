package com.example.carespawbe.dto.Shop.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResponse {
    private Long cartId;
    private Double cartTotalPrice;
//    private Double cartShippingFee;
//    private int cartTotalCoinEarned;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private Long userId;
    private String userFullName;
//    private Long voucherId;
//    private String voucherName;
    private List<CartItemResponse> cartItems;
}
