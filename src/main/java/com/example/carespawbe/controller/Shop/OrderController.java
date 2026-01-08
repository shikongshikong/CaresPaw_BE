package com.example.carespawbe.controller.Shop;

import com.example.carespawbe.dto.Shop.UserProductOrderTimeDTO;
import com.example.carespawbe.dto.Shop.request.OrderRequest;
import com.example.carespawbe.dto.Shop.response.OrderResponse;
import com.example.carespawbe.dto.Shop.response.ShopOrderResponse;
import com.example.carespawbe.service.Shop.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/checkout")
    public ResponseEntity<OrderResponse> checkout(
            HttpServletRequest request,
            @RequestBody OrderRequest body
    ) {
        Long userId = (Long) request.getAttribute("userId");

        if (userId == null) {
            throw new RuntimeException("User not authenticated");
        }

        OrderResponse response = orderService.checkout(userId, body);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{userId}")
    public ResponseEntity<?> getOrdersByUserId(@PathVariable Long userId) {
        List<OrderResponse> response = orderService.getOrderByUserId(userId);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/shop_order/{shopId}")
    public ResponseEntity<?> getShopOrdersByShop(@PathVariable Long shopId) {
        List<ShopOrderResponse> response = orderService.getShopOrdersByShop(shopId);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/shop_order/user/{userId}")
    public ResponseEntity<?> getShopOrderByUserId(@PathVariable Long userId) {
        List<ShopOrderResponse> response = orderService.getShopOrderByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/shop_order/{shopOrderId}/status")
    public ResponseEntity<?> updateShopOrderStatus(
            @PathVariable Long shopOrderId,
            @RequestParam Integer status
    ) {
        ShopOrderResponse res = orderService.updateShopOrderStatus(shopOrderId, status);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/user-product-times")
    public List<UserProductOrderTimeDTO> getUserProductTimes() {
        return orderService.findUserProductOrderTimes();
    }

}
