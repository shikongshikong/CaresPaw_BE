package com.example.carespawbe.entity.Shop;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSkuValueId implements Serializable {

    @Column(name = "product_sku_id")
    private Long productSkuId;

    @Column(name = "varriant_id")
    private Long varriantId;
}
