package com.example.carespawbe.controller;

import com.example.carespawbe.dto.request.ShopRequest;
import com.example.carespawbe.dto.response.ShopResponse;
import com.example.carespawbe.entity.Auth.UserEntity;
import com.example.carespawbe.repository.Auth.UserRepository;
import com.example.carespawbe.security.JwtService;
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
@RequestMapping("/shop")
@RequiredArgsConstructor
@MultipartConfig
@CrossOrigin(origins = "*")
public class ShopController {
    @Autowired
    private ShopService shopService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> registerShop(
            @RequestParam("shopName") String shopName,
            @RequestParam("shopAddress") String shopAddress,
            @RequestParam("shopPhoneNumber") String shopPhoneNumber,
            @RequestParam("userId") Long userId,
            @RequestParam("status") int status,
            @RequestParam(value = "shopLogo", required = false) MultipartFile shopLogo,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        try {
            //Tìm user trong DB
            UserEntity user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            //Nếu user đã là chủ shop thì chặn
            if (user.getRole() == 2) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "User already registered a shop"));
            }

            //Tạo ShopRequest để truyền xuống service
            ShopRequest shopRequest = new ShopRequest();
            shopRequest.setShopName(shopName);
            shopRequest.setShopAddress(shopAddress);
            shopRequest.setShopPhone(shopPhoneNumber);
            shopRequest.setUserId(userId);
            shopRequest.setStatus(1);

            ShopResponse shopResponse = shopService.registerShop(shopRequest, shopLogo, authorizationHeader);

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
            @RequestParam("shopPhoneNumber") String shopPhoneNumber,
            @RequestParam("userId") Long userId,
            @RequestParam(value = "shopLogo", required = false) MultipartFile shopLogo
    ){
        try {
            ShopRequest shopRequest = new ShopRequest();
            shopRequest.setShopName(shopName);
            shopRequest.setShopAddress(shopAddress);
            shopRequest.setShopPhone(shopPhoneNumber);
            shopRequest.setUserId(userId);

            ShopResponse shopResponse = shopService.updateShopInfo(userId, shopRequest, shopLogo);
            return ResponseEntity.ok(shopResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

//    show thông tin shop theo user
    @GetMapping("/user/{userId}")
    public ResponseEntity<ShopResponse> getShopByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(shopService.getShopByUserId(userId));
    }

    @GetMapping("/{shopId}")
    public ResponseEntity<ShopResponse> getShopById(@PathVariable Long shopId) {
        ShopResponse shop = shopService.getShopById(shopId);
        return ResponseEntity.ok(shop);
    }

    @GetMapping("/myShop")
    public ResponseEntity<?> getMyShop(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = authorizationHeader.startsWith("Bearer ")
                    ? authorizationHeader.substring(7)
                    : authorizationHeader;

            Long userId = jwtService.extractUserId(token);

            // ✅ reuse hàm cũ, không cần viết thêm service
            ShopResponse shop = shopService.getShopByUserId(userId);
            return ResponseEntity.ok(shop);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/myShop/id")
    public ResponseEntity<?> getMyShopId(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = authorizationHeader.startsWith("Bearer ")
                    ? authorizationHeader.substring(7)
                    : authorizationHeader;

            Long userId = jwtService.extractUserId(token);
            ShopResponse shop = shopService.getShopByUserId(userId);

            return ResponseEntity.ok(Map.of("shopId", shop.getShopId()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
