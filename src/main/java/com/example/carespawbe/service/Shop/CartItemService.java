package com.example.carespawbe.service.Shop;

import com.example.carespawbe.dto.Shop.request.CartItemRequest;
import com.example.carespawbe.dto.Shop.response.CartItemResponse;

import java.util.List;

public interface CartItemService {

    CartItemResponse addCartItem(Long cartId, CartItemRequest request);

    CartItemResponse updateCartItem(Long cartItemId, CartItemRequest request);

    void deleteCartItem(Long cartItemId);

    List<CartItemResponse> getCartItemsByCartId(Long cartId);

    CartItemResponse getCartItemById(Long cartItemId);

    // chuẩn SKU logic: nếu cart đã có skuId thì tăng quantity thay vì tạo dòng mới
    CartItemResponse upsertByCartIdAndSkuId(Long cartId, Long skuId, Integer quantity);
}
