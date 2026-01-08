package com.example.carespawbe.entity.Notification;

import com.example.carespawbe.entity.Auth.UserEntity;
import com.example.carespawbe.enums.NotificationType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name="actor_id")
    private Long actorId;

    @Enumerated(EnumType.STRING)
    @Column(name="type", nullable = false, length = 20)
    private NotificationType type;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 500)
    private String message;

    @Column(length = 300)
    private String link;

    @Column(name="image_url", length = 400)
    private String imageUrl;

    @Column(name="entity_type", length = 30)
    private String entityType;

    @Column(name="entity_id")
    private Long entityId;

    @Column(name="is_read", nullable = false)
    private Boolean isRead;

    @Column(name="created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name="read_at")
    private LocalDateTime readAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (isRead == null) isRead = false;
    }
}


