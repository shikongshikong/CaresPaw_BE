package com.example.carespawbe.dto.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor@Builder
public class ProductVarriantRequest {
    private Long varriantId;
    private String value;
    private Integer type;

}
