package com.example.carespawbe.dto.Shop.response;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VariantGroupResponse {
    private Long varriantId;
    private String varriantName;
    private List<Value> values;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Value {
        private Long varriantValueId;
        private String valueName;

        // ✅ tổng tồn kho của value này (cộng tất cả SKU chứa value này)
        private Integer totalStock;

        // (tuỳ chọn) để FE show giá range theo value
        private Double minPrice;
        private Double maxPrice;
    }
}
