package com.example.carespawbe.dto.Shop.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageProductResponse {
    private String imageProductUrl;
    private String imagePublicId;
}
