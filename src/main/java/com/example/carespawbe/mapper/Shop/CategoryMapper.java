package com.example.carespawbe.mapper.Shop;

import com.example.carespawbe.dto.Shop.request.CategoryRequest;
import com.example.carespawbe.dto.Shop.response.CategoryResponse;
import com.example.carespawbe.entity.Shop.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
public interface CategoryMapper {
    CategoryRequest toCategoryRequest(CategoryEntity categoryEntity);
    CategoryResponse toCategoryResponse(CategoryEntity categoryEntity);
    List<CategoryResponse> toResponseList(List<CategoryEntity> entities);
}
