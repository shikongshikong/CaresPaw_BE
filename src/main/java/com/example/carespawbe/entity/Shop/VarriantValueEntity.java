package com.example.carespawbe.entity.Shop;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "varriant_value")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VarriantValueEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "varriant_value_id")
    private Long varriantValueId;

    @Column(name = "value_name", nullable = false, columnDefinition = "NVARCHAR(50)")
    private String valueName;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "varriant_id")
    private VarriantEntity varriant;
}
