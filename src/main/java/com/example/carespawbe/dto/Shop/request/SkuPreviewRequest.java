package com.example.carespawbe.dto.Shop.request;

import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkuPreviewRequest {
    // ✅ BẮT BUỘC khi không còn product_varriant:
    // map varriantId -> list varriantValueId
    private Map<Long, List<Long>> selected;

    private Integer defaultStock;
}
