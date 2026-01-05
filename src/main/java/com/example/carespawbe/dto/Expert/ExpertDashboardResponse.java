package com.example.carespawbe.dto.Expert;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
public class ExpertDashboardResponse {
    // upcoming app, today remain app list, this month (total apps, total patients, total income, today apps)
    private UpComingApp upComingApp;
    private List<RemainApp> todayAppList;
    private DashBoardStatisticItem<Long> totalApps;
    private DashBoardStatisticItem<Long> totalPatients;
    private DashBoardStatisticItem<BigDecimal> totalIncome;
    private Long remainApps;

}
