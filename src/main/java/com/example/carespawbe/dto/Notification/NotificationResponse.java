package com.example.carespawbe.dto.Notification;

import com.example.carespawbe.enums.NotificationType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponse {
    private Long id;
    private NotificationType type;
    private String title;
    private String message;
    private String link;
    private String imageUrl;
    private Boolean isRead;
    private LocalDateTime createdAt;
}