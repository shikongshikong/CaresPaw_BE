package com.example.carespawbe.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVarriantResponse {
    private Long varriantId;
    private String productVarriantValue;
}
