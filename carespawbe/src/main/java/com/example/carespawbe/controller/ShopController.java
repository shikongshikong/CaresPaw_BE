package com.example.carespawbe.controller;

import com.example.carespawbe.dto.request.ShopRequest;
import com.example.carespawbe.dto.response.ShopResponse;
import com.example.carespawbe.service.ShopService;
import jakarta.servlet.annotation.MultipartConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/shop")
@RequiredArgsConstructor
@MultipartConfig
@CrossOrigin(origins = "*")
public class ShopController {
    @Autowired
    private ShopService shopService;

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> registerShop(
            @RequestParam("shopName") String shopName,
            @RequestParam("shopAddress") String shopAddress,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("userId") Long userId,
            @RequestParam("status") int status,
            @RequestParam(value = "shopLogo", required = false) MultipartFile shopLogo
    ){
        try {
            ShopRequest shopRequest = new ShopRequest();
            shopRequest.setShopName(shopName);
            shopRequest.setShopAddress(shopAddress);
            shopRequest.setShopPhone(phoneNumber);
            shopRequest.setUserId(userId);
            shopRequest.setStatus(1);

            ShopResponse shopResponse = shopService.registerShop(shopRequest, shopLogo);
            return ResponseEntity.ok(shopResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping(value = "/update/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateShop(
            @RequestParam("shopName") String shopName,
            @RequestParam("shopAddress") String shopAddress,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("userId") Long userId,
            @RequestParam(value = "shopLogo", required = false) MultipartFile shopLogo
    ){
        try {
            ShopRequest shopRequest = new ShopRequest();
            shopRequest.setShopName(shopName);
            shopRequest.setShopAddress(shopAddress);
            shopRequest.setShopPhone(phoneNumber);
            shopRequest.setUserId(userId);

            ShopResponse shopResponse = shopService.updateShopInfo(userId, shopRequest, shopLogo);
            return ResponseEntity.ok(shopResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ShopResponse> getShopByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(shopService.getShopByUserId(userId));
    }

}
