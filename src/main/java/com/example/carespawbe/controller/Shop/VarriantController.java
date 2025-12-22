package com.example.carespawbe.controller.Shop;

import com.example.carespawbe.dto.Shop.request.VarriantRequest;
import com.example.carespawbe.dto.Shop.response.VarriantResponse;
import com.example.carespawbe.entity.Shop.VarriantEntity;
import com.example.carespawbe.service.Shop.VarriantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/varriants")
@RequiredArgsConstructor
public class VarriantController {

    private final VarriantService varriantService;

    @GetMapping
    public ResponseEntity<List<VarriantResponse>> getAllVarriants() {
        return ResponseEntity.ok(varriantService.getAllVarriants());
    }

    // POST: Thêm mới
    @PostMapping("/create")
    public ResponseEntity<VarriantResponse> createVarriant(@RequestBody VarriantRequest request) {
        return ResponseEntity.ok(varriantService.createVarriant(request));
    }

    // PUT: Cập nhật
    @PutMapping("/{id}")
    public ResponseEntity<VarriantResponse> updateVarriant(@PathVariable Long id, @RequestBody VarriantRequest request) {
        return ResponseEntity.ok(varriantService.updateVarriant(id, request));
    }

    // DELETE: Xóa
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVarriant(@PathVariable Long id) {
        varriantService.deleteVarriant(id);
        return ResponseEntity.ok("Đã xóa biến thể thành công!");
    }
}
