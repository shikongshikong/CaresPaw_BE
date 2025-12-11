package com.example.carespawbe.mapper.Shop;

import com.example.carespawbe.dto.Shop.request.VoucherRequest;
import com.example.carespawbe.dto.Shop.response.VoucherResponse;
import com.example.carespawbe.entity.Shop.VoucherEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface VoucherMapper {

    @Mapping(target = "shopId", source = "shop.shopId")
    VoucherResponse toVoucherResponse(VoucherEntity entity);

    // Request -> Entity (không set shop ở đây vì shop lấy theo shopId/path)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "voucherId", ignore = true)
    @Mapping(target = "shop", ignore = true)
    @Mapping(target = "orderEntities", ignore = true)
    VoucherEntity toEntity(VoucherRequest request);

    // Update entity từ request
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "voucherId", ignore = true)
    @Mapping(target = "shop", ignore = true)
    @Mapping(target = "orderEntities", ignore = true)
    void updateEntity(@MappingTarget VoucherEntity entity, VoucherRequest request);
}
