package com.example.carespawbe.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PENDING_CONFIRMATION(0), // Chờ xác nhận
    AWAITING_PICKUP(1),       // Chờ lấy hàng
    SHIPPING(2),              // Vận chuyển
    AWAITING_DELIVERY(3),     // Chờ giao hàng
    COMPLETED(4),             // Hoàn thành
    CANCELLED(5),             // Đã huỷ
    RETURN_REFUND(6);         // Trả hàng/Hoàn tiền

    private final int value;
    OrderStatus(int value) { this.value = value; }
    public int getValue() { return value; }
}
