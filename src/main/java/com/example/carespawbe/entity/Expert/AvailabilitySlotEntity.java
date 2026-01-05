package com.example.carespawbe.entity.Expert;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "availability_slot")
@Getter
@Setter
public class AvailabilitySlotEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "slot_id")
    private Long id;

    private LocalDate date;

    @Column(name = "start_time", columnDefinition = "time")
//    @JdbcTypeCode(SqlTypes.TIME)
    private LocalTime startTime;

    @Column(name = "end_time", columnDefinition = "time")
//    @JdbcTypeCode(SqlTypes.TIME)
    private LocalTime endTime;
    private BigDecimal price;
    private Integer booked; // 0: empty, 1: booked, 2: cancel

    @OneToOne(mappedBy = "slot")
    private AppointmentEntity appointment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expert_id")
    private ExpertEntity expert;

    // trong AvailabilitySlotEntity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rule_id")
    private RecurrenceRuleEntity recurrenceRule;


    public AvailabilitySlotEntity(LocalDate date, LocalTime startTime, LocalTime endTime, BigDecimal price, Integer booked, ExpertEntity expert) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = price;
        this.booked = booked;
    }

    public AvailabilitySlotEntity() {

    }
}
