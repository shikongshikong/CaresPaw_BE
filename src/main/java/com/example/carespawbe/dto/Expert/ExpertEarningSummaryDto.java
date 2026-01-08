package com.example.carespawbe.dto.Expert;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class ExpertEarningSummaryDto {
    private BigDecimal total;
    private BigDecimal fee;
    private BigDecimal net;
    private BigDecimal unpaidAmount;
    private Long unpaidCount;


    public ExpertEarningSummaryDto(BigDecimal total, BigDecimal fee, BigDecimal net,
                                   BigDecimal unpaidAmount, Long unpaidCount) {
        this.total = total;
        this.fee = fee;
        this.net = net;
        this.unpaidAmount = unpaidAmount;
        this.unpaidCount = unpaidCount;
    }

}

