package com.example.carespawbe.controller.Admin;

import com.example.carespawbe.dto.Admin.AdminUserItemResponse;
import com.example.carespawbe.service.Admin.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping
    public ResponseEntity<List<AdminUserItemResponse>> listUsers(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Integer state
    ) {
        return ResponseEntity.ok(adminUserService.listUsers(q, state));
    }

    /**
     * Ban/Unban:
     * PATCH /admin/users/{id}/state?value=0  (ban)
     * PATCH /admin/users/{id}/state?value=1  (unban)
     */
    @PatchMapping("/{id}/state")
    public ResponseEntity<?> updateState(@PathVariable Long id, @RequestParam Integer value) {
        adminUserService.updateState(id, value);
        return ResponseEntity.ok().build();
    }
}
