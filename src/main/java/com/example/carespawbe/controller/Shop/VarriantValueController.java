package com.example.carespawbe.controller.Shop;

import com.example.carespawbe.dto.Shop.request.VarriantValueRequest;
import com.example.carespawbe.dto.Shop.response.VarriantValueResponse;
import com.example.carespawbe.service.Shop.VarriantValueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/varriants/values")
@RequiredArgsConstructor
public class VarriantValueController {

    private final VarriantValueService service;

    @GetMapping("/{varriantId}")
    public ResponseEntity<List<VarriantValueResponse>> getByVarriant(@PathVariable Long varriantId) {
        return ResponseEntity.ok(service.getValuesByVarriantId(varriantId));
    }

    @PostMapping("/create")
    public ResponseEntity<VarriantValueResponse> create(@RequestBody VarriantValueRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VarriantValueResponse> update(@PathVariable Long id, @RequestBody VarriantValueRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("Đã xóa varriant value!");
    }
}
