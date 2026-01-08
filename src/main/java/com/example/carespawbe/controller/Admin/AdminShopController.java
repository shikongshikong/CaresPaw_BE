package com.example.carespawbe.controller.Admin;

import com.example.carespawbe.dto.Shop.response.ShopResponse;
import com.example.carespawbe.service.Shop.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/shops")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdminShopController {

    private final ShopService shopService;

    @GetMapping("")
    public Page<ShopResponse> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return shopService.getAllShopsForAdmin(page, size);
    }

    @PutMapping("/{id}/lock")
    public void lock(@PathVariable Long id) {
        shopService.lockShop(id);
    }

    @PutMapping("/{id}/unlock")
    public void unlock(@PathVariable Long id) {
        shopService.unlockShop(id);
    }
}
