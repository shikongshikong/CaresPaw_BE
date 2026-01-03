package com.example.carespawbe.dto.Expert;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ExpertDashboardResponse {
    // upcoming app, today remain app list, this month (total apps, total patients, total income, today apps)
    private UpComingApp upComingApp;

    private List<RemainApp> todayAppList;
    private DashBoardStatisticItem totalApps;
    private DashBoardStatisticItem totalPatients;
    private DashBoardStatisticItem totalIncome;
    private long remainApps;

}
