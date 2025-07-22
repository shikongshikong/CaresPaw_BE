package com.example.carespawbe.mapper;

import com.example.carespawbe.dto.Forum.ForumPostCategoryRequest;
import com.example.carespawbe.entity.ForumPostToCategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ForumPostCategoryMapper {

    List<ForumPostToCategoryEntity> toPocategoryList(List<ForumPostCategoryRequest> forumPostCategoryRequestList);

    @Mapping(source = "postId", target = "forumPostEntity.id")
    @Mapping(source = "categoryId", target = "forumPostCategoryEntity.id")
    ForumPostToCategoryEntity toPostCategoryEntity(ForumPostCategoryRequest forumPostCategoryRequest);

}
