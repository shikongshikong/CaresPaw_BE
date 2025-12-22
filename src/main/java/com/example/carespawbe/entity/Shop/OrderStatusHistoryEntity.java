package com.example.carespawbe.entity.Shop;

import com.example.carespawbe.entity.Auth.UserEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "order_status_history",
        indexes = {
                @Index(name = "IX_osh_order_id", columnList = "order_id")
        }
)
public class OrderStatusHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_status_history_id")
    private Long id;

    @Column(name = "from_status")
    private int fromStatus;

    @Column(name = "to_status", nullable = false)
    private int toStatus;

    @Column(name = "note")
    private String note;

    @Column(name = "changed_at", nullable = false)
    private LocalDateTime changedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

    // FK: order_status_history.changed_by_user_id -> users.user_id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "changed_by_user_id")
    private UserEntity changedBy;

}
