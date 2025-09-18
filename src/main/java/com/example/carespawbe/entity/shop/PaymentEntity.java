package com.example.carespawbe.entity.shop;
import com.example.carespawbe.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "payment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;

    @Column(nullable = false)
    private String payment_method;

    @Column(nullable = false)
    private Double amount_before;

    @Column(nullable = false)
    private int paymentCoinUsed;

    @Column(nullable = false)
    private Double amount_after;

    @Column(nullable = false)
    private int paymentStatus;

    @Column(nullable = false)
    private LocalDate paymentCreatedAt;

    @Column(nullable = true)
    private LocalDate paymentUpdatedAt;

    @OneToMany(mappedBy = "paymentEntity")
    private List<OrderEntity> orderEntities;
}
