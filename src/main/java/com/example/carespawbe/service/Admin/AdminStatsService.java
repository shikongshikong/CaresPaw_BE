package com.example.carespawbe.service.Admin;

import com.example.carespawbe.dto.Admin.AdminDashboardStatsResponse;
import com.example.carespawbe.dto.Admin.AdminYearTrendResponse;

import java.util.List;

public interface AdminStatsService {
    AdminDashboardStatsResponse getDashboardStats();

    List<AdminYearTrendResponse> getYearTrend(int year);
}
