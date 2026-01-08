package com.example.carespawbe.dto.Notification;

import com.example.carespawbe.enums.NotificationType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationCreateRequest {
    private Long userId;
    private Long actorId;
    private NotificationType type;
    private String title;
    private String message;
    private String link;
    private String imageUrl;
    private String entityType;
    private Long entityId;
}
