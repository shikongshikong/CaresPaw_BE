package com.example.carespawbe.dto.Shop.request;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkuBulkUpdateRequest {
    private Integer stock;
    private BigDecimal price;
}
