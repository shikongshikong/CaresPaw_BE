package com.example.carespawbe.entity.Expert;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "expert_to_category")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ExpertToExpertCategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @ManyToOne()
    @JoinColumn(name = "expert_id", nullable = false)
    private ExpertEntity expert;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "expert_category_id", nullable = false)
    private ExpertCategoryEntity expertCategory;

    public ExpertToExpertCategoryEntity(ExpertEntity expertEntity, ExpertCategoryEntity category) {
        this.expert = expertEntity;
        this.expertCategory = category;
    }
}
