package com.example.carespawbe.entity.Shop;

import com.example.carespawbe.entity.Auth.UserEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "shop_order_status_history",
        indexes = {
                @Index(name = "IX_sosh_shop_order_id", columnList = "shop_order_id")
        }
)
public class ShopOrderStatusHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shop_order_status_history_id")
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
    @JoinColumn(name = "shop_order_id", nullable = false)
    private ShopOrderEntity shopOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "changed_by_user_id")
    private UserEntity changedBy;
}
