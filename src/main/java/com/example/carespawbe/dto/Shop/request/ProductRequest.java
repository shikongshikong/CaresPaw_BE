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
//    private String productDescribe;
    private Double productPrice;
//    private Double productPriceSale;
    private Integer productAmount;
    private Integer productStatus;
    private String productUsing;
    private Long categoryId;
    private Long shopId;
    private List<ProductVarriantRequest> productVarriants;
    private List<String> imageUrls;
    private LocalDate productCreatedAt;
    private LocalDate productUpdatedAt;

}