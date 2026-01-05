package com.example.carespawbe.entity.Forum;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "forum_post_to_category")
@AllArgsConstructor
@NoArgsConstructor
@Builder
//@Data
@Getter
@Setter
public class ForumPostToCategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @ManyToOne()
    @JoinColumn(name = "postId", nullable = false)
    private ForumPostEntity forumPostEntity;

    @JsonBackReference
    @ManyToOne()
    @JoinColumn(name = "categoryId", nullable = false)
    private ForumPostCategoryEntity forumPostCategoryEntity;
}
