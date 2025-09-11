package com.example.carespawbe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "user_save_forum_post")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ForumPostSaveEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate savedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @OneToOne
    @JoinColumn(name = "post_id", nullable = false)
    private ForumPostEntity forumPostEntity;

    @PrePersist
    protected void onCreate() {
        savedAt = LocalDate.now();
    }
}
