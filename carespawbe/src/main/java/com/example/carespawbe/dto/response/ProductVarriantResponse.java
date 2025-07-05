package com.example.carespawbe.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVarriantResponse {
    private Long id;
    private String value;
    private Integer type;
    private VarriantResponse varriant;
}
