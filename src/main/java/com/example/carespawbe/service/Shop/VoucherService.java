package com.example.carespawbe.service.Shop;

import com.example.carespawbe.dto.Shop.request.VoucherRequest;
import com.example.carespawbe.dto.Shop.response.VoucherResponse;

import java.time.LocalDate;
import java.util.List;

public interface VoucherService {

    VoucherResponse createVoucher(Long shopId, VoucherRequest request);

    VoucherResponse updateVoucher(Long voucherId, VoucherRequest request);

    void deleteVoucher(Long voucherId);

    VoucherResponse getVoucherById(Long voucherId);

    // Shop owner: xem tất cả voucher của shop mình
    List<VoucherResponse> getAllVoucherByShop(Long shopId);

    // User checkout: lấy voucher đang dùng được theo shop
    List<VoucherResponse> getAvailableVouchersByShop(Long shopId, LocalDate today);

    // Dùng lúc checkout: validate + trừ số lượng
    VoucherResponse applyVoucher(Long shopId, Long voucherId, LocalDate today);
}
