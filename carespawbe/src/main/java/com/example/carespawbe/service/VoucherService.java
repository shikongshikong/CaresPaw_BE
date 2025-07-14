package com.example.carespawbe.service;

import com.example.carespawbe.dto.request.VoucherRequest;
import com.example.carespawbe.dto.response.VoucherResponse;

import java.util.List;

public interface VoucherService {
    VoucherResponse createVoucher(VoucherRequest request);
    VoucherResponse updateVoucher(Long voucherId, VoucherRequest request);
    void deleteVoucher(Long voucherId);
    VoucherResponse getVoucherById(Long voucherId);
    List<VoucherResponse> getAllVouchers();
    boolean isVoucherValid(String voucherName);

    List<VoucherResponse> getAllVoucherByShop(Long shopId);
}

