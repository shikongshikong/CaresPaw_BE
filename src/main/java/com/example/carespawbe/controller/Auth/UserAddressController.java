package com.example.carespawbe.controller.Auth;

import com.example.carespawbe.dto.Auth.UserAddressRequest;
import com.example.carespawbe.dto.Auth.UserAddressResponse;
import com.example.carespawbe.service.Auth.UserAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/addresses")
public class UserAddressController {

    private final UserAddressService service;

    private Long currentUserId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("Unauthenticated");
        }

        // Vì JwtAuthenticationFilter set principal = userId (String)
        Object principal = auth.getPrincipal();
        if (principal == null) throw new RuntimeException("Missing principal");

        try {
            return Long.valueOf(principal.toString());
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid userId in principal: " + principal);
        }
    }

    @GetMapping
    public List<UserAddressResponse> list() {
        return service.list(currentUserId());
    }

    @GetMapping("/{addressId}")
    public UserAddressResponse get(@PathVariable Long addressId) {
        return service.get(currentUserId(), addressId);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public UserAddressResponse create(@RequestBody UserAddressRequest req) {
        return service.create(currentUserId(), req);
    }

    @PutMapping("/{addressId}")
    public UserAddressResponse update(@PathVariable Long addressId,
                                      @RequestBody UserAddressRequest req) {
        return service.update(currentUserId(), addressId, req);
    }

    @DeleteMapping("/{addressId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long addressId) {
        service.delete(currentUserId(), addressId);
    }

    @PatchMapping("/{addressId}/default")
    public UserAddressResponse setDefault(@PathVariable Long addressId) {
        return service.setDefault(currentUserId(), addressId);
    }
}
