package com.example.carespawbe.serviceImp.Shop;

import com.example.carespawbe.entity.Shop.ShopOrderEntity;
import com.example.carespawbe.enums.OrderStatus;
import com.example.carespawbe.repository.Shop.OrderRepository;
import com.example.carespawbe.repository.Shop.ShopOrderRepository;
import com.example.carespawbe.service.Shop.ShopOrderService;
//import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShopOrderServiceImp implements ShopOrderService {

    private final ShopOrderRepository shopOrderRepository;
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public void updateShopOrderStatus(Long shopOrderId, Integer newStatus) {
        ShopOrderEntity so = shopOrderRepository.findById(shopOrderId)
                .orElseThrow(() -> new RuntimeException("Shop order not found"));

        so.setShopOrderStatus(newStatus);
        shopOrderRepository.save(so);

        Long orderId = so.getOrder().getOrderId();
        syncOrderStatus(orderId);
    }

    private void syncOrderStatus(Long orderId) {
        List<Integer> statuses = shopOrderRepository.findStatusesByOrderId(orderId);
        if (statuses == null || statuses.isEmpty()) return;

        int minStatus = statuses.stream()
                .mapToInt(Integer::intValue)
                .min()
                .orElse(OrderStatus.PENDING_CONFIRMATION.getValue());

        int updated = orderRepository.updateOrderStatus(orderId, minStatus, LocalDate.now());

        // ✅ thêm dòng này để test ngay trên console
        System.out.println("SYNC orderId=" + orderId + " -> status=" + minStatus + " | updatedRows=" + updated);
    }
}
