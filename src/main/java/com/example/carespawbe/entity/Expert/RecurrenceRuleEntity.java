package com.example.carespawbe.entity.Expert;

import com.example.carespawbe.enums.RecurrenceMode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "recurrence_rule")
@Getter
@Setter
public class RecurrenceRuleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rule_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecurrenceMode mode; // NONE/DAILY/WEEKLY/MONTHLY

    /**
     * CSV weekdays: "MO,TU,WE" (only used when WEEKLY)
     */
    @Column(name = "by_week_days", length = 50)
    private String byWeekDays;

    /**
     * Apply until (inclusive)
     */
    @Column(name = "until_date")
    private LocalDate until;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * Slot "gốc" đại diện cho series này (optional nhưng useful)
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "root_slot_id", unique = true)
    private AvailabilitySlotEntity rootSlot;

    @PrePersist
    void prePersist() {
        createdAt = LocalDateTime.now();
        if (mode == null) mode = RecurrenceMode.NONE;
    }
}

