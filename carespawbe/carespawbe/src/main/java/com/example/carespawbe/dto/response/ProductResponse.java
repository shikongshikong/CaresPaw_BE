package com.example.carespawbe.dto.response;

import lombok.*;

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
    private Long shopId;
    private List<String> productVarriants;
    private List<String> imageUrls;
}

