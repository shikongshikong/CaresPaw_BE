package com.example.carespawbe.service.Shop;

import com.example.carespawbe.dto.Shop.request.CartItemRequest;
import com.example.carespawbe.dto.Shop.response.CartResponse;

public interface CartService {

    // tạo hoặc lấy cart của user (1 user 1 cart)
    CartResponse getOrCreateCartByUserId(Long userId);

    // lấy cart + items + sku + product + images + variant text
    CartResponse getCartByUserId(Long userId);

    // clear cart (xóa toàn bộ items)
    void clearCart(Long cartId);

    // xóa cart (hiếm khi cần, thường chỉ clear)
    void deleteCart(Long cartId);

    // thao tác nhanh trên cart (cart-level command)
    CartResponse addItem(Long userId, CartItemRequest request);          // request chứa skuId + quantity
    CartResponse updateItem(Long userId, Long cartItemId, CartItemRequest request);
    CartResponse removeItem(Long userId, Long cartItemId);
}
