package com.example.carespawbe.service.Shop;

import com.example.carespawbe.dto.Shop.request.CartItemRequest;
import com.example.carespawbe.dto.Shop.request.CartRequest;
import com.example.carespawbe.dto.Shop.response.CartItemResponse;
import com.example.carespawbe.dto.Shop.response.CartResponse;
import com.example.carespawbe.dto.Shop.response.ImageProductResponse;
import com.example.carespawbe.dto.Shop.response.ProductVarriantResponse;

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