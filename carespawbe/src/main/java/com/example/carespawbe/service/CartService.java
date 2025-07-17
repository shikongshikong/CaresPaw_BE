package com.example.carespawbe.service;

import com.example.carespawbe.dto.request.CartRequest;
import com.example.carespawbe.dto.response.CartResponse;

import java.util.List;

public interface CartService {
    CartResponse createCart(CartRequest request);
    CartResponse updateCart(Long cartId, CartRequest request);
    void deleteCart(Long cartId);
    CartResponse getCartByUserId(Long userId);
//    List<CartResponse> getAllCarts();
}
