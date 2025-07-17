package com.example.carespawbe.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoucherRequest {
    private Long shopId;
    private String voucherName;
    private String voucherDescribe;
    private Double voucherValue;
    private String voucherType;
    private LocalDate startedAt;
    private LocalDate finishedAt;
    private int voucherAmount;
    private int voucherStatus;
}

