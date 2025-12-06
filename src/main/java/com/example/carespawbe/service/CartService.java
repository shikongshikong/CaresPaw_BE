package com.example.carespawbe.service;

import com.example.carespawbe.dto.request.CartItemRequest;
import com.example.carespawbe.dto.request.CartRequest;
import com.example.carespawbe.dto.response.CartItemResponse;
import com.example.carespawbe.dto.response.CartResponse;
import com.example.carespawbe.dto.response.ImageProductResponse;
import com.example.carespawbe.dto.response.ProductVarriantResponse;
import com.example.carespawbe.entity.shop.ImageProductEntity;

import java.util.List;

public interface CartService {
    CartResponse createCart(CartRequest request);
    CartResponse updateCart(Long cartId, CartRequest request);
    CartItemResponse updateCartItem(Long cartId, Long cartItemId, CartItemRequest request);
    void deleteCart(Long cartId);
    CartResponse getCartByUserId(Long userId);
    List<ProductVarriantResponse> getCartProductsVariantsByProductId(Long productId);
    List<ImageProductResponse> getImageProduct(Long productId);
//    List<CartResponse> getAllCarts();
}