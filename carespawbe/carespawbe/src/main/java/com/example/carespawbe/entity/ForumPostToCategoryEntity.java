package com.example.carespawbe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "post_to_category")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ForumPostToCategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "postId", nullable = false)
    private ForumPostEntity forumPostEntity;

    @ManyToOne()
    @JoinColumn(name = "categoryId", nullable = false)
    private ForumPostCategoryEntity forumPostCategoryEntity;
}
