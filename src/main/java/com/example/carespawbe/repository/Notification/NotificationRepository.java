package com.example.carespawbe.repository.Notification;

import com.example.carespawbe.entity.Notification.NotificationEntity;
import com.example.carespawbe.enums.NotificationType;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    @Query("""
        SELECT n FROM NotificationEntity n
        WHERE n.user.id = :userId
          AND (:type IS NULL OR n.type = :type)
          AND (:unreadOnly = false OR n.isRead = false)
        ORDER BY n.createdAt DESC
    """)
    Page<NotificationEntity> findByFilter(Long userId, NotificationType type, boolean unreadOnly, Pageable pageable);

    long countByUser_IdAndIsReadFalse(Long userId);

    @Modifying
    @Query("UPDATE NotificationEntity n SET n.isRead=true, n.readAt=:t WHERE n.user.id=:u AND n.id=:id")
    int markRead(Long u, Long id, LocalDateTime t);

    @Modifying
    @Query("UPDATE NotificationEntity n SET n.isRead=true, n.readAt=:t WHERE n.user.id=:u AND n.id IN :ids")
    int markReadBatch(Long u, List<Long> ids, LocalDateTime t);

    @Modifying
    @Query("UPDATE NotificationEntity n SET n.isRead=true, n.readAt=:t WHERE n.user.id=:u AND n.isRead=false")
    int markAllRead(Long u, LocalDateTime t);
}
