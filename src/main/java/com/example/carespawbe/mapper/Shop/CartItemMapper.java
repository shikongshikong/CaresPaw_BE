package com.example.carespawbe.mapper.Shop;

import com.example.carespawbe.dto.Shop.response.CartItemResponse;
import com.example.carespawbe.entity.Shop.CartItemEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = { ProductMapper.class },
        nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CartItemMapper {

    @Mapping(target = "product", source = "productSku.product") // ✅ lấy product từ SKU
    @Mapping(target = "productSkuId", source = "productSku.productSkuId")
    @Mapping(target = "skuCode", source = "skuCode")           // snapshot
    @Mapping(target = "variantText", source = "variantText")   // snapshot
    CartItemResponse toCartItemResponse(CartItemEntity entity);

    List<CartItemResponse> toResponseList(List<CartItemEntity> entities);
}
