package com.example.carespawbe.mapper.Shop;

import com.example.carespawbe.dto.Shop.response.FeedbackResponse;
import com.example.carespawbe.dto.Shop.response.ShopOrderResponse;
import com.example.carespawbe.entity.Shop.FeedbackEntity;
import com.example.carespawbe.entity.Shop.ShopOrderEntity;
import com.example.carespawbe.mapper.Auth.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;
@Mapper(
        componentModel = "spring",
        uses = { OrderItemMapper.class, UserMapper.class}, // Bỏ OrderMapper ra
        nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface FeedbackMapper {
    @Mapping(source = "shop.shopId", target = "shopId")
//    @Mapping(source = "order.orderId", target = "orderId") // Lấy ID của order gán vào

    FeedbackResponse toResponse(FeedbackEntity entity);

    List<FeedbackResponse> toResponseList(List<FeedbackEntity> entities);
}
