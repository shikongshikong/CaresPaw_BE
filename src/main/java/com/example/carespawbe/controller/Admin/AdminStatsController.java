package com.example.carespawbe.controller.Admin;

import com.example.carespawbe.dto.Admin.AdminDashboardStatsResponse;
import com.example.carespawbe.dto.Admin.AdminYearTrendResponse;
import com.example.carespawbe.service.Admin.AdminStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdminStatsController {

    private final AdminStatsService adminStatsService;

    @GetMapping("/stats")
    public AdminDashboardStatsResponse getStats() {
        return adminStatsService.getDashboardStats();
    }

    @GetMapping("/stats/trend")
    public List<AdminYearTrendResponse> getYearTrend(@RequestParam(required = false) Integer year) {
        int y = (year != null) ? year : LocalDate.now().getYear();
        return adminStatsService.getYearTrend(y);
    }
}
