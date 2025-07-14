package com.example.carespawbe.service;

import com.example.carespawbe.dto.request.CartItemRequest;
import com.example.carespawbe.dto.response.CartItemResponse;

import java.util.List;

public interface CartItemService {
    CartItemResponse addCartItem(CartItemRequest request);
    CartItemResponse updateCartItem(Long cartItemId, CartItemRequest request);
    void deleteCartItem(Long cartItemId);
    List<CartItemResponse> getCartItemsByCartId(Long cartId);
    CartItemResponse getCartItemById(Long cartItemId);
}
