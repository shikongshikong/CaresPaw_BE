package com.example.carespawbe.mapper.Shop;

import com.example.carespawbe.dto.Shop.response.ShopOrderResponse;
import com.example.carespawbe.entity.Shop.ShopOrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;
// import OrderMapper, @Autowired, @Lazy không cần thiết nữa nếu làm cách này

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = { OrderItemMapper.class, VoucherMapper.class, ProductMapper.class,  }, // Bỏ OrderMapper ra
        nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ShopOrderMapper {
    // Dùng Interface bình thường lại, không cần Abstract class nữa cho gọn

    @Mapping(source = "shop.shopId", target = "shopId")
    @Mapping(source = "order.orderId", target = "orderId") // Lấy ID của order gán vào
    ShopOrderResponse toResponse(ShopOrderEntity entity);

    List<ShopOrderResponse> toResponseList(List<ShopOrderEntity> entities);
}