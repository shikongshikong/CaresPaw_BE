package com.example.carespawbe.entity.shop;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "varriants")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VarriantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "varriant_id")
    private Long varriantId;

    @Column(nullable = false)
    private String varriantName;

    @OneToMany(mappedBy = "varriants")
    private List<ProductVarriantEntity> varriantList;
}
