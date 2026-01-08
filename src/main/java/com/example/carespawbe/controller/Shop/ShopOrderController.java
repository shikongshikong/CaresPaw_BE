package com.example.carespawbe.controller.Shop;

import com.example.carespawbe.dto.Shop.response.ShopOrderResponse;
import com.example.carespawbe.service.Shop.OrderService;
import com.example.carespawbe.service.Shop.ShopOrderService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shopOrders")
@RequiredArgsConstructor
public class ShopOrderController {

    private final ShopOrderService shopOrderService;
    private final OrderService orderService;

    @GetMapping("/{id}")
    public ShopOrderResponse getDetail(@PathVariable Long id){
        return orderService.getShopOrderDetail(id);
    }

    @PutMapping("/{shopOrderId}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long shopOrderId,
            @RequestBody UpdateStatusRequest req
    ) {
        orderService.updateShopOrderStatus(shopOrderId, req.getStatus());
        return ResponseEntity.ok("Updated");
    }

    @Data
    public static class UpdateStatusRequest {
        private Integer status;
    }
}
