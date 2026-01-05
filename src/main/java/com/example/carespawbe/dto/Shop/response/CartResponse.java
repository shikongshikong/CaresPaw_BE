package com.example.carespawbe.dto.Shop.response;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResponse {
    private Long cartId;
    private Double cartTotalPrice;

    private LocalDate createdAt;
    private LocalDate updatedAt;

    private Long userId;
    private String userFullName;

    private List<CartItemResponse> cartItems;
}
