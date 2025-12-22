package com.example.carespawbe.entity.Shop;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class FeedbackMediaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedbackMediaId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "feedbackId", nullable = false)
    @JsonIgnore
    private FeedbackEntity feedback;

    @Column(nullable = false, length = 255)
    private String publicId;

    @Column(nullable = false, length = 10)
    private String resourceType;

    @Column(nullable = false)
    private String secureUrl;

    @Column(nullable = false, updatable = false)
    private LocalDate createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDate.now();
    }
}
