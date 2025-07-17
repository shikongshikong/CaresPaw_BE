package com.example.carespawbe.mapper;

import com.example.carespawbe.dto.request.VoucherRequest;
import com.example.carespawbe.dto.response.VoucherResponse;
import com.example.carespawbe.entity.shop.VoucherEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
public interface VoucherMapper {
    @Mapping(target = "shopId", source = "shop.shopId")
    VoucherRequest toVoucherRequest(VoucherEntity voucherEntity);

    @Mapping(target = "shopId", source = "shop.shopId")
    @Mapping(target = "shopName", source = "shop.shopName")
    VoucherResponse toVoucherResponse(VoucherEntity voucherEntity);

    List<VoucherResponse> toVoucherResponseList(List<VoucherEntity> voucherEntities);
}
