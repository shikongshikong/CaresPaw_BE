package com.example.carespawbe.dto.Shop.response;

import java.time.LocalDate;

public record ProductInfoDTO(
        Long productId,
        String productName,
        String productUsing,
        LocalDate productCreatedAt,
        Long categoryId,
        Long shopId
) {}
