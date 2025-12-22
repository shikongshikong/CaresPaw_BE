package com.example.carespawbe.dto.Shop.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoucherResponse {
    private Long voucherId;
    private Long shopId;
    private String voucherName;
    private String voucherDescribe;
    private Double voucherValue;
    private String voucherType;
    private LocalDate startedAt;
    private LocalDate finishedAt;
    private int voucherAmount;
    private int voucherStatus;
    private int voucherMinOrder;
    private int voucherUsageType;
}
