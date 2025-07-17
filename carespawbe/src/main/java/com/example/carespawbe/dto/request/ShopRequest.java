package com.example.carespawbe.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShopRequest {
    private String shopName;
    private String shopAddress;
    private String shopLogo;
    private String shopPhone;
    private Long userId;
    private int status;
}
