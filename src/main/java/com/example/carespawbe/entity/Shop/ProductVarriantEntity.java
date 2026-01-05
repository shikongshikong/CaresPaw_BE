//package com.example.carespawbe.entity.Shop;
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.util.List;
//
//@Entity
//@Table(name = "product_varriant")
//@Data
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class ProductVarriantEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "product_varriant_id")
//    private Long productVarriantId;
//
//    @Column(nullable = false, columnDefinition = "NVARCHAR(50)")
//    private String productVarriantValue;
//
//    @ManyToOne
//    @JsonIgnore
//    @JoinColumn(name = "product_id")
//    private ProductEntity productVarriants;
//
//    @ManyToOne
//    @JsonIgnore
//    @JoinColumn(name = "varriant_id")
//    private VarriantEntity varriants;
//
//    @ManyToOne
//    @JsonIgnore
//    @JoinColumn(name = "varriant_value_id")
//    private VarriantValueEntity varriantValue;
//
////    @OneToMany(mappedBy = "productVarriantEntity")
////    private List<OrderItemEntity> orderItemEntityList;
//}
