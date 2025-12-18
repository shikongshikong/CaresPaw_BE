package com.example.carespawbe.mapper.Shop;

import com.example.carespawbe.dto.Shop.response.OrderResponse;
import com.example.carespawbe.entity.Shop.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = { ShopOrderMapper.class },
        nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface OrderMapper {
    OrderResponse toResponse(OrderEntity entity);
    List<OrderResponse> toResponseList(List<OrderEntity> entities);
}