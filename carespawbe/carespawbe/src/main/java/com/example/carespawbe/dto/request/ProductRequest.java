package com.example.carespawbe.dto.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest {
    private String name;
    private String describe;
    private Double price;
    private Integer amount;
    private Integer status;
    private String using;
    private Long categoryId;
    private Long shopId;
}
