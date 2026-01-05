package com.example.carespawbe.entity.Forum;

import com.example.carespawbe.entity.Auth.UserEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity()
//@Data
@Getter
@Setter
@Table(name = "forum_post_history")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ForumPostHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long id;
    private LocalDate createdAt;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private ForumPostEntity forumPostEntity;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDate.now();
    }
}
