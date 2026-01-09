package com.example.carespawbe.dto.Shop;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForecastResponse {
    private double[] forecast;
    private Integer trainLen;
    private Integer predictN;
}