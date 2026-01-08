package com.example.carespawbe.dto.Admin;

import com.example.carespawbe.dto.Expert.CertificateResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class AdminExpertVerifyItemResponse {
    private Long expertId;
    private Long userId;

    private String fullName;
    private String idImage;

    private LocalDate createdAt;
    private Integer status;

    // ✅ chuyên môn = category mà user chọn khi đăng ký
    private List<String> categories;

    // ✅ tất cả chứng chỉ đã upload
    private List<CertificateResponse> certificates;
}
