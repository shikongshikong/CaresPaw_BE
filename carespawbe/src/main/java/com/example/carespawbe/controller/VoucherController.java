package com.example.carespawbe.controller;

import com.example.carespawbe.dto.request.VoucherRequest;
import com.example.carespawbe.dto.response.VoucherResponse;
import com.example.carespawbe.service.VoucherService;
import jakarta.servlet.annotation.MultipartConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/voucher")
@RequiredArgsConstructor
@MultipartConfig
@CrossOrigin(origins = "*")
public class VoucherController {

    @Autowired
    private VoucherService voucherService;

    @PostMapping("/add")
    public ResponseEntity<?> createVoucher(
            @RequestParam("voucherName") String voucherName,
            @RequestParam("voucherDescribe") String voucherDescribe,
            @RequestParam("voucherValue") Double voucherValue,
            @RequestParam("voucherType") String voucherType,
            @RequestParam("startedAt")LocalDate startedAt,
            @RequestParam("finishedAt") LocalDate finishedAt,
            @RequestParam("voucherAmount") int voucherAmount,
            @RequestParam("voucherStatus") int voucherStatus,
            @RequestParam("shopId") Long shopId
    ) {
        try {
            VoucherRequest voucherRequest = new VoucherRequest();
            voucherRequest.setVoucherName(voucherName);
            voucherRequest.setVoucherDescribe(voucherDescribe);
            voucherRequest.setVoucherValue(voucherValue);
            voucherRequest.setVoucherType(voucherType);
            voucherRequest.setStartedAt(startedAt);
            voucherRequest.setFinishedAt(finishedAt);
            voucherRequest.setVoucherAmount(voucherAmount);
            voucherRequest.setVoucherStatus(voucherStatus);
            voucherRequest.setShopId(shopId);

            VoucherResponse voucherResponse = voucherService.createVoucher(voucherRequest);
            return ResponseEntity.ok(voucherResponse);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/shop/{shopId}")
    public ResponseEntity<?> getAllVoucherByShop(@PathVariable Long shopId) {
        try {
            List<VoucherResponse> list = voucherService.getAllVoucherByShop(shopId);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Không thể lấy danh sách voucher theo shop"));
        }
    }

    @GetMapping("/{voucherId}")
    public ResponseEntity<?> getVoucherById(@PathVariable Long voucherId) {
        try {
            VoucherResponse response = voucherService.getVoucherById(voucherId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Đã xảy ra lỗi khi lấy voucher."));
        }
    }

    @DeleteMapping("/delete/{voucherId}")
    public ResponseEntity<?> deleteVoucher(@PathVariable Long voucherId) {
        try {
            voucherService.deleteVoucher(voucherId);
            return ResponseEntity.ok(Map.of("message", "Voucher deleted successfully"));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }

    }

    @PutMapping("/update/{voucherId}")
    public ResponseEntity<?> updateVoucher(
            @PathVariable Long voucherId,
            @RequestParam("voucherName") String voucherName,
            @RequestParam("voucherDescribe") String voucherDescribe,
            @RequestParam("voucherValue") Double voucherValue,
            @RequestParam("voucherType") String voucherType,
            @RequestParam("startedAt")LocalDate startedAt,
            @RequestParam("finishedAt") LocalDate finishedAt,
            @RequestParam("voucherAmount") int voucherAmount,
            @RequestParam("voucherStatus") int voucherStatus,
            @RequestParam("shopId") Long shopId
    ) {
        try {
            VoucherRequest voucherRequest = new VoucherRequest();
            voucherRequest.setVoucherName(voucherName);
            voucherRequest.setVoucherDescribe(voucherDescribe);
            voucherRequest.setVoucherValue(voucherValue);
            voucherRequest.setVoucherType(voucherType);
            voucherRequest.setStartedAt(startedAt);
            voucherRequest.setFinishedAt(finishedAt);
            voucherRequest.setVoucherAmount(voucherAmount);
            voucherRequest.setVoucherStatus(voucherStatus);
            voucherRequest.setShopId(shopId);

            VoucherResponse voucherResponse = voucherService.updateVoucher(voucherId, voucherRequest);
            return ResponseEntity.ok(voucherResponse);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<VoucherResponse>> getAllVoucher() {
        return ResponseEntity.ok(voucherService.getAllVouchers());
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateVoucher(@RequestParam("voucherName") String voucherName) {
        boolean isValid = voucherService.isVoucherValid(voucherName);
        return ResponseEntity.ok(Map.of("voucherValid", isValid));
    }

}
