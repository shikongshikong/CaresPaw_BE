package com.example.carespawbe.entity.Forum;

import com.example.carespawbe.entity.Auth.UserEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "forum_post")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ForumPostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    private String title;

    @Column(columnDefinition = "VARCHAR(MAX)")
    private String content;

    private LocalDate createAt;
    private LocalDate updateAt;
    private int state;
    private int typeId; // new field
    private Long viewedAmount;
    private Long commentedAmount;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @JsonManagedReference
    @OneToMany(mappedBy = "forumPostEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ForumPostHistoryEntity> histories;

    //
    @JsonManagedReference
    @OneToMany(mappedBy = "forumPostEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ForumPostSaveEntity> forumPostSaveEntity;

    @JsonManagedReference
    @OneToMany(mappedBy = "forumPostEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ForumPostCommentEntity> comments;

    @JsonManagedReference
    @OneToMany(mappedBy = "forumPostEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ForumPostToCategoryEntity> toCategories;

    @JsonManagedReference
    @OneToMany(mappedBy = "forumPostEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ForumPostLikeEntity> forumPostLikeEntity;

    @PrePersist
    protected void onCreate() {
        createAt = LocalDate.now();
        updateAt = LocalDate.now();
        viewedAmount = 0L;
        commentedAmount = 0L;
    }
}
