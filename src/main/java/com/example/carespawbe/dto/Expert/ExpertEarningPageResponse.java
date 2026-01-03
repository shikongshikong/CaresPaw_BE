package com.example.carespawbe.dto.Expert;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ExpertEarningPageResponse {
    private List<ExpertEarningItemDto> items;
    private int page; // 1-based
    private int size;
    private long total;
    private ExpertEarningSummaryDto summary;

    public ExpertEarningPageResponse() {}

    public ExpertEarningPageResponse(List<ExpertEarningItemDto> items, int page, int size, long total, ExpertEarningSummaryDto summary) {
        this.items = items;
        this.page = page;
        this.size = size;
        this.total = total;
        this.summary = summary;
    }

}

