package com.example.carespawbe.mapper.Notification;

import com.example.carespawbe.dto.Notification.NotificationResponse;
import com.example.carespawbe.entity.Notification.NotificationEntity;

public class NotificationMapper {

    private NotificationMapper() {}

    public static NotificationResponse toResponse(NotificationEntity n) {
        return NotificationResponse.builder()
                .id(n.getId())
                .type(n.getType())
                .title(n.getTitle())
                .message(n.getMessage())
                .link(n.getLink())
                .imageUrl(n.getImageUrl())
                .isRead(n.getIsRead())
                .createdAt(n.getCreatedAt())
                .build();
    }
}
