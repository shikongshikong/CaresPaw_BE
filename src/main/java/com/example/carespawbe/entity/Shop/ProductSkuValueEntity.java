package com.example.carespawbe.entity.Shop;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_sku_value")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSkuValueEntity {

    @EmbeddedId
    private ProductSkuValueId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productSkuId")
    @JsonIgnore
    @JoinColumn(name = "product_sku_id", nullable = false)
    private ProductSkuEntity productSku;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("varriantId")
    @JsonIgnore
    @JoinColumn(name = "varriant_id", nullable = false)
    private VarriantEntity varriant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "varriant_value_id", nullable = false)
    private VarriantValueEntity varriantValue;
}
