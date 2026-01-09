package com.example.carespawbe.dto.Shop;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RevenueTimelineDTO {
    private Integer year;
    private Integer month;
    private Double actualRevenue;   // dữ liệu thật (đơn COMPLETED)
    private Double forecastRevenue; // sample (hoặc null)
}
