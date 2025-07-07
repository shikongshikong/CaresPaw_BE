package com.example.carespawbe.dto.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShopResponse {
    private Long shopId;
    private String shopName;
    private String shopAddress;
    private String shopLogo;
    private String email;
    private String phoneNumber;
}
