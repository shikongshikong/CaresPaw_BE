package com.example.carespawbe.dto.Auth;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAddressRequest {
    // ✅ thay vì nhận UserEntity, FE chỉ gửi userId (hoặc BE tự lấy từ token)
    private Long userId;

    // ✅ trùng tên với entity
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
}
