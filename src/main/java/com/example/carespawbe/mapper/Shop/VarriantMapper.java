package com.example.carespawbe.mapper.Shop;

import com.example.carespawbe.dto.Shop.request.VarriantRequest;
import com.example.carespawbe.dto.Shop.response.VarriantResponse;
import com.example.carespawbe.entity.Shop.VarriantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring" , nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
public interface VarriantMapper {
    // 1. Chuyển từ Entity sang Response (GET)
    VarriantResponse toVarriantResponse(VarriantEntity varriant);
    // 2. Chuyển từ Request sang Entity (CREATE)
    VarriantEntity toVarriantEntity(VarriantRequest varriantRequest);
    // 3. Chuyển danh sách Entity sang danh sách Response (GET ALL)
    List<VarriantResponse> toVarriantResponseList(List<VarriantEntity> varriantEntityList);

}
