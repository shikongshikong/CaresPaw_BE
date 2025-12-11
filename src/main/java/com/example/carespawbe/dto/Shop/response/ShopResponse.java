package com.example.carespawbe.dto.Shop.response;

import lombok.*;

import java.time.LocalDate;

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
    private LocalDate created_at;
    private LocalDate updated_at;
}
