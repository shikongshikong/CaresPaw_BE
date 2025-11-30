package com.example.carespawbe.dto.request;

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
public class CartRequest {
    private Double cartTotalPrice;
    private Double cartShippingFee;
    private int cartTotalCoinEarned;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private Long userId;
    private Long voucherId;
    private List<CartItemRequest> cartItems;
}
