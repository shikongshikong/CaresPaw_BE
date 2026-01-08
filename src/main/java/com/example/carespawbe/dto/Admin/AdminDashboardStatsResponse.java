package com.example.carespawbe.dto.Admin;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDashboardStatsResponse {
    private Double revenue;
    private Long ordersToday;
    private Long totalShops;
    private Long totalExperts;
    private Long totalUsers;
    private Long postsToday;
}
