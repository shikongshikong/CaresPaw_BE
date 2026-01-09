package com.example.carespawbe.dto.Shop.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCardResponse {
    private Long productId;
    private String productName;
    private Double productPrice;

    private Double rating;
    private Long sold;

    private Long shopId;
    private String shopName;
    private String shopLogo;

    private Long categoryId;
    private String categoryName;

    // ✅ ảnh đại diện (1 ảnh) để card load nhanh
    private String thumbnailUrl;
}
