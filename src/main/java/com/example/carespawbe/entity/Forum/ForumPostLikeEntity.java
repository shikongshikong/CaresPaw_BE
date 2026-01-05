package com.example.carespawbe.entity.Forum;

import com.example.carespawbe.entity.Auth.UserEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
//@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "user_like_forum_post")
public class ForumPostLikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private ForumPostEntity forumPostEntity;

    private int status;

}
