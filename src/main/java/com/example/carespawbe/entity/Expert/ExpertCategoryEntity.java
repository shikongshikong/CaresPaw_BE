package com.example.carespawbe.entity.Expert;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Expert_Category")
@Setter
@Getter
public class ExpertCategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expert_category_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "expertCategory")
    private List<ExpertToExpertCategoryEntity> expertToExpertCategoryEntities = new ArrayList<>();

}
