package com.example.carespawbe.entity.Shop;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
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

    @Column(nullable = false, columnDefinition = "NVARCHAR(50)")
    private String varriantName;

    @OneToMany(mappedBy = "varriants")
    private List<ProductVarriantEntity> varriantList;

    @OneToMany(mappedBy = "varriant", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<VarriantValueEntity> values = new ArrayList<>();

}
