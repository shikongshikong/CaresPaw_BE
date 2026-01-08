package com.example.carespawbe.serviceImp.Notification;

import com.example.carespawbe.dto.Notification.MarkReadRequest;
import com.example.carespawbe.dto.Notification.NotificationCreateRequest;
import com.example.carespawbe.dto.Notification.NotificationPageResponse;
import com.example.carespawbe.dto.Notification.NotificationResponse;
import com.example.carespawbe.entity.Auth.UserEntity;
import com.example.carespawbe.entity.Notification.NotificationEntity;
import com.example.carespawbe.entity.Shop.ShopEntity;
import com.example.carespawbe.enums.NotificationType;
import com.example.carespawbe.mapper.Notification.NotificationMapper;
import com.example.carespawbe.repository.Notification.NotificationRepository;
import com.example.carespawbe.repository.Auth.UserRepository;
import com.example.carespawbe.repository.Shop.ShopRepository;
import com.example.carespawbe.service.Notification.NotificationService;
import com.example.carespawbe.utils.SecurityUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repo;
    private final UserRepository userRepo;

    @Autowired
    private ShopRepository shopRepository;

    public NotificationServiceImpl(NotificationRepository r, UserRepository u) {
        this.repo = r;
        this.userRepo = u;
    }

    public NotificationPageResponse getMyNotifications(NotificationType type, boolean unreadOnly, int page, int size) {
        Long uid = SecurityUtils.getCurrentUserId();
        Page<NotificationEntity> p = repo.findByFilter(uid, type, unreadOnly, PageRequest.of(page, size));

        return NotificationPageResponse.builder()
                .content(p.getContent().stream().map(NotificationMapper::toResponse).collect(Collectors.toList()))
                .page(p.getNumber()).size(p.getSize())
                .totalElements(p.getTotalElements()).totalPages(p.getTotalPages())
                .unreadCount(repo.countByUser_IdAndIsReadFalse(uid))
                .build();
    }

    public long getMyUnreadCount() {
        return repo.countByUser_IdAndIsReadFalse(SecurityUtils.getCurrentUserId());
    }

    @Transactional public void markRead(Long id) {
        repo.markRead(SecurityUtils.getCurrentUserId(), id, LocalDateTime.now());
    }

    @Transactional public void markReadBatch(MarkReadRequest r) {
        repo.markReadBatch(SecurityUtils.getCurrentUserId(), r.getIds(), LocalDateTime.now());
    }

    @Transactional public int markAllRead() {
        return repo.markAllRead(SecurityUtils.getCurrentUserId(), LocalDateTime.now());
    }

    public NotificationResponse create(NotificationCreateRequest req) {
        UserEntity u = userRepo.findById(req.getUserId()).orElseThrow();
        String imageUrl = resolveImageUrl(req);
        NotificationEntity n = NotificationEntity.builder()
                .user(u)
                .actorId(req.getActorId())
                .type(req.getType())
                .title(req.getTitle())
                .message(req.getMessage())
                .link(req.getLink())
                .imageUrl(imageUrl)
                .entityType(req.getEntityType())
                .entityId(req.getEntityId())
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();
        return NotificationMapper.toResponse(repo.save(n));
    }

    private String resolveImageUrl(NotificationCreateRequest req) {
        if (req.getImageUrl() != null && !req.getImageUrl().isBlank()) return req.getImageUrl();

        NotificationType type = req.getType();

        if (type == NotificationType.SYSTEM) {
            return "system.png"; // bạn đổi theo static của bạn
        }

        // actor avatar
        String actorAvatar = null;
        if (req.getActorId() != null) {
            actorAvatar = userRepo.findById(req.getActorId())
                    .map(UserEntity::getAvatar)
                    .orElse(null);
        }

        if (type == NotificationType.FORUM || type == NotificationType.EXPERT) {
            return (actorAvatar != null && !actorAvatar.isBlank()) ? actorAvatar : "no-avatar-img.png";
        }

        if (type == NotificationType.SHOP) {
            if ("SHOP".equalsIgnoreCase(req.getEntityType()) && req.getEntityId() != null) {
                String logo = shopRepository.findById(req.getEntityId())
                        .map(ShopEntity::getShopLogo)
                        .orElse(null);
                if (logo != null && !logo.isBlank()) return logo;
            }
            return (actorAvatar != null && !actorAvatar.isBlank()) ? actorAvatar : "no-avatar-img.png";
        }

        return (actorAvatar != null && !actorAvatar.isBlank()) ? actorAvatar : "no-avatar-img.png";
    }
}
