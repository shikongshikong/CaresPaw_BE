package com.example.carespawbe.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PENDING(0),      // Chờ xác nhận
    CONFIRMED(1),    // Đã xác nhận / Đang đóng gói
    SHIPPING(2),     // Đang giao hàng (Cập nhật vận chuyển ở đây)
    DELIVERED(3),    // Giao thành công
    CANCELLED(4);    // Đã hủy

    private final int value;
    OrderStatus(int value) { this.value = value; }
    public int getValue() { return value; }
}
