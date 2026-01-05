package com.example.carespawbe.entity.Forum;

import com.example.carespawbe.entity.Auth.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
//@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "forum_post_comment")
public class ForumPostCommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate createAt;
    private String content;
//    private Long likedAmount;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private ForumPostEntity forumPostEntity;

    @PrePersist
    protected void onCreate() {
        createAt = LocalDate.now();
    }
}
