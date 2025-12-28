package com.example.carespawbe.dto.Shop.response;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {
    private Long productId;
    private String productName;
    private String productDescribe;
    private Double productPrice;
    private Integer productAmount;
    private Integer productStatus;
    private String productUsing;
    private Long categoryId;
    private String categoryName;
    private Long shopId;
    private String shopName;
    private List<ProductVarriantResponse> productVarriants;
    private List<String> imageUrls;
    private String productVideoUrl;
    private LocalDate productUpdatedAt;
    private LocalDate productCreatedAt;

    private Long sold;
    private Double rating;


}

