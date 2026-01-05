package com.example.carespawbe.dto.Shop.request;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkuGenerateRequest {
    private Integer defaultStock;
    private BigDecimal defaultPrice;

    // nếu FE gửi từng dòng (combo + stock + price)
    private List<SkuItem> variants;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SkuItem {
        private List<Long> varriantValueIds;
        private Integer stock;
        private BigDecimal price;
    }
}
