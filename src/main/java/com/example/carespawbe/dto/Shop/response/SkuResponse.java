package com.example.carespawbe.dto.Shop.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkuResponse {
    private Long productSkuId;
    private String skuCode;
    private String skuName;
    private Integer stock;
    private BigDecimal price;
    private Boolean isActive;
    private Long sold;

    // ✅ MUST NOT NULL
    private List<Long> varriantValueIds;
}
