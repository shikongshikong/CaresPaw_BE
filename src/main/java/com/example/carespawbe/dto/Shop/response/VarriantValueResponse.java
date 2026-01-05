package com.example.carespawbe.dto.Shop.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VarriantValueResponse {
    private Long varriantValueId;
    private Long varriantId;
    private String varriantName;
    private String valueName;
    private Boolean isActive;
}
