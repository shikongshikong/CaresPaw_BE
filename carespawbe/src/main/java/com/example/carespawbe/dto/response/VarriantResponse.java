package com.example.carespawbe.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VarriantResponse {
    private Long varriantId;
    private String varriantName;
}
