package com.example.carespawbe.dto.Admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminYearTrendResponse {
    private Integer month;   // 1..12
    private Long orders;     // số đơn trong tháng
    private Double revenue;  // doanh thu (COMPLETED) trong tháng
}
