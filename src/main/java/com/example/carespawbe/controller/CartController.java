package com.example.carespawbe.controller;

import com.example.carespawbe.dto.request.CartItemRequest;
import com.example.carespawbe.dto.request.CartRequest;
import com.example.carespawbe.dto.response.CartItemResponse;
import com.example.carespawbe.dto.response.CartResponse;
import com.example.carespawbe.dto.response.ImageProductResponse;
import com.example.carespawbe.dto.response.ProductVarriantResponse;
import com.example.carespawbe.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // Thêm giỏ hàng
    @PostMapping("/add")
    public ResponseEntity<CartResponse> createCart(
            @RequestBody CartRequest cartRequest
    ) {
        try {
            return ResponseEntity.ok(cartService.createCart(cartRequest));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Cập nhật giỏ hàng
    @PutMapping("/update")
    public ResponseEntity<CartResponse> updateCart(
            @RequestParam("cartId") Long cartId,
            @RequestBody CartRequest cartRequest
    ) {
        try {
            return ResponseEntity.ok(cartService.updateCart(cartId, cartRequest));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Xoá giỏ hàng
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteCart(@RequestParam("cartId") Long cartId) {
        cartService.deleteCart(cartId);
        return ResponseEntity.ok("Xoá thành công.");
    }

    // Lấy giỏ hàng theo userId
    @GetMapping("/get/{userId}")
    public ResponseEntity<CartResponse> getCartByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }
    // Lấy danh sách ảnh sản phẩm
    @GetMapping("/product/{productId}/images")
    public ResponseEntity<List<ImageProductResponse>> getImageProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(cartService.getImageProduct(productId));
    }

    // Lấy danh sách biến thể sản phẩm
    @GetMapping("/product/{productId}/variants")
    public ResponseEntity<List<ProductVarriantResponse>> getProductVariants(@PathVariable Long productId) {
        return ResponseEntity.ok(cartService.getCartProductsVariantsByProductId(productId));
    }
    // Cập nhật sản phẩm trong giỏ hàng
    @PutMapping("/{cartId}/item/{cartItemId}")
    public ResponseEntity<CartItemResponse> updateCartItem(
            @PathVariable Long cartId,
            @PathVariable Long cartItemId,
            @RequestBody CartItemRequest request) {
        try {
             CartItemResponse response = cartService.updateCartItem(cartId, cartItemId, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
