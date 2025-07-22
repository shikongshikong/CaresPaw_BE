package com.example.carespawbe.mapper;

import com.example.carespawbe.dto.History.ForumPostSideBarResponse;
import com.example.carespawbe.entity.ForumPostSaveEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ForumPostSaveMapper {

//    @Mapping(source = "userId", target = "userEntity.id")
//    @Mapping(source = "postId", target = "forumPostEntity.id")
//    ForumPostSaveEntity toSaveEntity(ForumPostDetailRequest postDetailRequest);

    @Mapping(source = "forumPostEntity.title", target = "title")
    @Mapping(source = "forumPostEntity.id", target = "postId")
    @Mapping(source = "forumPostEntity.viewedAmount", target = "viewedAmount")
    @Mapping(source = "forumPostEntity.commentedAmount", target = "commentedAmount")
    @Mapping(source = "forumPostEntity.user.avatar", target = "avatar")
    @Mapping(source = "forumPostEntity.user.id", target = "userId")
    @Mapping(source = "forumPostEntity.user.fullname", target = "fullname")
    @Mapping(target = "saved", expression = "java(forumPostSaveEntity != null)")
    ForumPostSideBarResponse toSideBarResponse(ForumPostSaveEntity forumPostSaveEntity);

    List<ForumPostSideBarResponse> toSaveResponseList(List<ForumPostSaveEntity> forumPostSaveEntities);
}
