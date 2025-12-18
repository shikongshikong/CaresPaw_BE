package com.example.carespawbe.mapper.Shop;

import com.example.carespawbe.dto.Shop.response.ShopOrderResponse;
import com.example.carespawbe.entity.Shop.ShopOrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = { OrderItemMapper.class },
        nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ShopOrderMapper {
    ShopOrderResponse toResponse(ShopOrderEntity entity);
    List<ShopOrderResponse> toResponseList(List<ShopOrderEntity> entities);
}
