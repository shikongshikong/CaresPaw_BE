package com.example.carespawbe.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_varriant")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVarriantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_varriant_id")
    private Long productVarriantId;

    @Column(nullable = false)
    private String value;

    @Column(nullable = false)
    private Integer type;

    @ManyToOne
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private ProductEntity productVarriants;

    @ManyToOne
    @JoinColumn(name = "varriant_id", insertable = false, updatable = false)
    private VarriantEntity varriants;
}
