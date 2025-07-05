package com.example.carespawbe.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest {
    private String productName;
    private String productDescribe;
    private Double productPrice;
    private Integer productAmount;
    private Integer productStatus;
    private String productUsing;
    private Long categoryId;
    private Long shopId;
    private List<ProductVarriantRequest> productVarriants;
    private List<String> imageUrls;
}