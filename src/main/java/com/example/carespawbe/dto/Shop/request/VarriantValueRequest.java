package com.example.carespawbe.dto.Shop.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VarriantValueRequest {
    private Long varriantId;
    private String valueName;
    private Boolean isActive;
}
