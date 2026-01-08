package com.example.carespawbe.controller.Admin;

import com.example.carespawbe.dto.Admin.AdminExpertVerifyItemResponse;
import com.example.carespawbe.service.Admin.AdminExpertVerifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/experts")
@RequiredArgsConstructor
public class AdminExpertVerifyController {

    private final AdminExpertVerifyService adminExpertVerifyService;

    /**
     * ✅ GET /admin/experts?status=
     * - status=null  -> lấy TẤT CẢ (0/1/2)
     * - status=0     -> PENDING
     * - status=1     -> ACTIVE
     * - status=2     -> BLOCK
     */
    @GetMapping
    public ResponseEntity<List<AdminExpertVerifyItemResponse>> list(
            @RequestParam(required = false) Integer status
    ) {
        return ResponseEntity.ok(adminExpertVerifyService.list(status));
    }

    // ✅ (optional) giữ lại endpoint cũ cho pending nếu FE đang dùng
    @GetMapping("/pending")
    public ResponseEntity<List<AdminExpertVerifyItemResponse>> listPending() {
        return ResponseEntity.ok(adminExpertVerifyService.listPending());
    }

    // ✅ PUT: duyệt
    @PutMapping("/{expertId}/approve")
    public ResponseEntity<?> approve(@PathVariable Long expertId) {
        adminExpertVerifyService.approve(expertId);
        return ResponseEntity.ok().build();
    }

    // ✅ PUT: từ chối
    @PutMapping("/{expertId}/reject")
    public ResponseEntity<?> reject(@PathVariable Long expertId) {
        adminExpertVerifyService.reject(expertId);
        return ResponseEntity.ok().build();
    }
}
