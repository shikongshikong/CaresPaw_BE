package com.example.carespawbe.mapper.Shop;

import com.example.carespawbe.dto.Shop.response.OrderItemResponse;
import com.example.carespawbe.entity.Shop.OrderItemEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = { ProductMapper.class },
        nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface OrderItemMapper {

    @Mapping(target = "product", source = "productSku.product") // ✅ product từ SKU
    @Mapping(target = "productSkuId", source = "productSku.productSkuId")
    @Mapping(target = "skuCode", source = "skuCode")
    @Mapping(target = "variantText", source = "variantText")
    @Mapping(target = "shopOrderId", source = "shopOrder.shopOrderId")
    OrderItemResponse toResponse(OrderItemEntity entity);

    List<OrderItemResponse> toResponseList(List<OrderItemEntity> entities);
}
