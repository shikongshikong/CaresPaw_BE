package com.example.carespawbe.entity.Expert;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "expert_earning")
@Setter
@Getter
public class ExpertEarningEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "earning_id")
    private Long id;

    private BigDecimal totalEarning;
    private BigDecimal platformFee; // fee % with system
    private BigDecimal expertGain; // total - fee
    private Integer status; // 0: not pay yet, 1: paid for expert
    private LocalDateTime createAt;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "app_id")
    private AppointmentEntity appointmentEntity;

    @ManyToOne
    @JoinColumn(name = "expert_id")
    private ExpertEntity expertEntity;

}
