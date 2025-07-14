package com.example.carespawbe.dto.response;

import jakarta.persistence.Column;
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
    private Double productPriceSale;
    private Integer productAmount;
    private Integer productStatus;
    private String productUsing;
    private Long categoryId;
    private Long shopId;
//    private List<String> productVarriants;
    private List<ProductVarriantResponse> productVarriants;
    private List<String> imageUrls;
//    private List<ImageProductResponse> imageUrls;
    private String productVideoUrl;
    private LocalDate productUpdatedAt;
    private LocalDate productCreatedAt;


}

