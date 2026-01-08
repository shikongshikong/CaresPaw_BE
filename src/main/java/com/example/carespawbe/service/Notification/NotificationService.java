package com.example.carespawbe.service.Notification;

import com.example.carespawbe.dto.Notification.MarkReadRequest;
import com.example.carespawbe.dto.Notification.NotificationCreateRequest;
import com.example.carespawbe.dto.Notification.NotificationPageResponse;
import com.example.carespawbe.dto.Notification.NotificationResponse;
import com.example.carespawbe.enums.NotificationType;

public interface NotificationService {
    NotificationPageResponse getMyNotifications(NotificationType type, boolean unreadOnly, int page, int size);
    long getMyUnreadCount();
    void markRead(Long id);
    void markReadBatch(MarkReadRequest req);
    int markAllRead();
    NotificationResponse create(NotificationCreateRequest req);
}
