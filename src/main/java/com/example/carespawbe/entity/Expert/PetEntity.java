package com.example.carespawbe.entity.Expert;

import com.example.carespawbe.entity.Auth.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "pet")
@Setter
@Getter
public class PetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pet_id")
    private Long id;

    private String name;
    private String species;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;


}
