package com.example.carespawbe.dto.Shop.request;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest {
    private String productName;
    private Double productPrice;     // base price (optional – có thể dùng làm defaultPrice khi generate SKU)
    private Integer productAmount;   // base amount (optional – hoặc bỏ nếu chỉ dùng stock theo SKU)
    private Integer productStatus;
    private String productUsing;
    private Long categoryId;

    // ⚠️ shopId nên lấy từ token, không cần FE gửi (nếu vẫn giữ thì ignore BE)
    private Long shopId;

    private List<String> imageUrls;
    private LocalDate productCreatedAt;
    private LocalDate productUpdatedAt;
}
