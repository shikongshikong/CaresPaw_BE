package com.example.carespawbe.dto.Shop.response;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDetailResponse {
    private Long productId;
    private String productName;
    private Double productPrice;
    private Integer productAmount;
    private Integer productStatus;
    private String productUsing;

    private Long categoryId;
    private String categoryName;

    private Long shopId;
    private String shopName;
    private String shopLogo;

    private List<String> imageUrls;
    private String productVideoUrl;

    private LocalDate productUpdatedAt;
    private LocalDate productCreatedAt;

    private Long sold;
    private Double rating;

    // ✅ FE render biến thể + disable option hết hàng
    private List<VariantGroupResponse> variantGroups;

    // ✅ FE match theo selectedValueIds để đổi giá/tồn kho và chặn mua
    private List<SkuResponse> skus;
}
