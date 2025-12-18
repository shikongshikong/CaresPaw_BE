package com.example.carespawbe.dto.Auth;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAddressResponse {
    private Long addressId;

    // ✅ trả userId thay cho user object (tránh lazy load / vòng lặp JSON)
    private Long userId;

    private String receiverName;
    private String receiverPhone;

    private Integer provinceId;
    private Integer provinceCode;
    private String provinceName;

    private Integer districtId;
    private Integer districtCode;
    private String districtName;

    private String wardCode;
    private String wardName;

    private String detail;

    private boolean isDefault;
    private boolean isDeleted;
}
