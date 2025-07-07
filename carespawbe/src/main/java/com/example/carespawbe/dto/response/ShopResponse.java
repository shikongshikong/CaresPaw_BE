package com.example.carespawbe.dto.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShopResponse {
    private Long id;
    private String shopName;
    private String shopAddress;
    private String logo;
    private String email;
    private String phoneNumber;
}
