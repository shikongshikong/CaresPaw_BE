package com.example.carespawbe.service.Shop;

import com.example.carespawbe.dto.Shop.RevenueTimelineDTO;
import com.example.carespawbe.dto.Shop.UserProductOrderTimeDTO;
import com.example.carespawbe.dto.Shop.request.OrderRequest;
import com.example.carespawbe.dto.Shop.response.OrderResponse;
import com.example.carespawbe.dto.Shop.response.ShopOrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse checkout(Long userId, OrderRequest request);
    List<OrderResponse> getOrderByUserId(Long userId);

    List<ShopOrderResponse> getShopOrdersByShop(Long shopId);
    List<ShopOrderResponse> getShopOrderByUserId(Long userId);
//    List<ShopOrderResponse> updateOrderStatus(Long userId);
    ShopOrderResponse updateShopOrderStatus(Long shopOrderId, Integer newStatus);

    ShopOrderResponse getShopOrderDetail(Long id);

    List<UserProductOrderTimeDTO> findUserProductOrderTimes();

    List<RevenueTimelineDTO> getRevenueTimeline(Long shopId, int monthsBack, int monthsForward);
}