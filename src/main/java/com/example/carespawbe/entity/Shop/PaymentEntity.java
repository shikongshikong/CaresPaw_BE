package com.example.carespawbe.entity.Shop;
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

    @Column(nullable = false, columnDefinition = "NVARCHAR(100)")
    private String paymentMethod;

    @Column(nullable = false)
    private Double pricePayment;
    private Long paymentCode;
    private String description;

    @Column(nullable = false)
    private LocalDate paymentCreatedAt;


    @OneToOne(mappedBy = "payment")
    private OrderEntity order;
}
