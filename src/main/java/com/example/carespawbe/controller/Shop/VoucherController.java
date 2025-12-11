package com.example.carespawbe.controller.Shop;

import com.example.carespawbe.dto.Shop.request.VoucherRequest;
import com.example.carespawbe.dto.Shop.response.VoucherResponse;
import com.example.carespawbe.service.Shop.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/voucher")
public class VoucherController {

    private final VoucherService voucherService;

    // =========================
    // 1) SHOP OWNER - MANAGER
    // =========================

    // Create voucher for a shop (shop owner)
    @PostMapping("/shop/{shopId}/create_voucher")
    public ResponseEntity<VoucherResponse> create(
            @PathVariable Long shopId,
            @RequestBody VoucherRequest request
    ) {
        return ResponseEntity.ok(voucherService.createVoucher(shopId, request));
    }

    @PutMapping("/{voucherId}")
    public ResponseEntity<VoucherResponse> update(
            @PathVariable Long voucherId,
            @RequestBody VoucherRequest request
    ) {
        return ResponseEntity.ok(voucherService.updateVoucher(voucherId, request));
    }

    @DeleteMapping("/{voucherId}")
    public ResponseEntity<?> delete(@PathVariable Long voucherId) {
        voucherService.deleteVoucher(voucherId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{voucherId}")
    public ResponseEntity<VoucherResponse> getById(@PathVariable Long voucherId) {
        return ResponseEntity.ok(voucherService.getVoucherById(voucherId));
    }

    @GetMapping("/shop/{shopId}/list_vouchers")
    public ResponseEntity<List<VoucherResponse>> getAllByShop(@PathVariable Long shopId) {
        return ResponseEntity.ok(voucherService.getAllVoucherByShop(shopId));
    }

    // =========================
    // 2) USER CHECKOUT
    // =========================

    // Lấy voucher available theo shop để hiển thị trong trang order (mỗi shop-order 1 list)
    @GetMapping("/shop/{shopId}/vouchers/available")
    public ResponseEntity<List<VoucherResponse>> getAvailableByShop(
            @PathVariable Long shopId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {
        return ResponseEntity.ok(voucherService.getAvailableVouchersByShop(shopId, date));
    }

    // Apply voucher lúc checkout (validate + trừ amount)
    @PostMapping("/shop/{shopId}/vouchers/{voucherId}/apply")
    public ResponseEntity<VoucherResponse> applyVoucher(
            @PathVariable Long shopId,
            @PathVariable Long voucherId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {
        return ResponseEntity.ok(voucherService.applyVoucher(shopId, voucherId, date));
    }
}
