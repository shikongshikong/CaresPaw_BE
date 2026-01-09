package com.example.carespawbe.dto.Shop;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UnitsTimelineResponse {
    private Integer year;
    private Integer month;
    private Long actualUnits;
}
