package com.example.carespawbe.dto.Shop;

import java.time.LocalDate;

public record UserProductOrderTimeDTO(
        Long userId,
        Long productId,
        LocalDate orderCreatedAt
) {}
