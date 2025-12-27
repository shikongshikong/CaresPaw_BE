package com.example.carespawbe.entity.Forum;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "forum_post_type")
public class ForumPostTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String image;

    @JsonManagedReference
    @OneToMany(mappedBy = "typeEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ForumPostEntity> forumPostEntities;

}
