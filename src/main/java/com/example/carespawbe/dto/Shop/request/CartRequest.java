package com.example.carespawbe.dto.Shop.request;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartRequest {
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private Long userId;

    private List<CartItemRequest> cartItems;
}
