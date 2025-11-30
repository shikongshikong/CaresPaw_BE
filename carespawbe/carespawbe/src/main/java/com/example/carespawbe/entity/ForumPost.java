package com.example.carespawbe.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "forum_post")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ForumPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    private String title;
    private String content;
    private LocalDate createAt;
    private LocalDate updateAt;
    private String state;
    private String type; // new field
    private Long viewedAmount;
    private Long commentedAmount;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<ForumPostHistory> histories = new ArrayList<>();

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL)
    private ForumPostSave forumPostSave;

    @PrePersist
    protected void onCreate() {
        createAt = LocalDate.now();
        updateAt = LocalDate.now();
        viewedAmount = 0L;
        commentedAmount = 0L;
    }
}
