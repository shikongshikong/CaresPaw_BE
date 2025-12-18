package com.example.carespawbe.service.Shop;

import com.example.carespawbe.dto.Shop.request.OrderRequest;
import com.example.carespawbe.dto.Shop.response.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse checkout(Long userId, OrderRequest request);
    List<OrderResponse> getOrderByUserId(Long userId);
}