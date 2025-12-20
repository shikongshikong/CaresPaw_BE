package com.example.carespawbe.mapper.Shop;

import com.example.carespawbe.dto.Shop.response.OrderItemResponse;
import com.example.carespawbe.entity.Shop.OrderItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = { ProductMapper.class },
        nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface OrderItemMapper {
    @Mapping(source = "product", target = "product") // Dòng này sẽ kích hoạt ProductMapper.toProductResponse()
    @Mapping(source = "shopOrder.shopOrderId", target = "shopOrderId")
    OrderItemResponse toResponse(OrderItemEntity entity);
    List<OrderItemResponse> toResponseList(List<OrderItemEntity> entities);
}
