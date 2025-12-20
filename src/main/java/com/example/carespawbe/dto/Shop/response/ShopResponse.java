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
    private String shopDescription;
    private String shopLogo;
    private String shopBanner;
    private String email;
    private String phoneNumber;
    private LocalDate created_at;
    private LocalDate updated_at;
    private int status;
    private Integer districtId;
}
