package com.example.carespawbe.mapper;

import com.example.carespawbe.dto.Forum.ForumPostRequest;
import com.example.carespawbe.dto.Forum.ForumPostResponse;
import com.example.carespawbe.dto.UserProfile.UserPostResponse;
import com.example.carespawbe.entity.ForumPostEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ForumPostMapper {
    /*@Mapping(source = "typeId", target = "typeId")*/
    @Mapping(source = "userId", target = "user.id")
    ForumPostEntity toPostEntity(ForumPostRequest forumPostRequest);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.fullname", target = "fullname")
    @Mapping(source = "user.avatar", target = "avatar")
    @Mapping(target = "saved", expression = "java(forumPostEntity.getForumPostSaveEntity() != null)")
    ForumPostResponse toPostResponse(ForumPostEntity forumPostEntity);

    @Mapping(source = "typeId", target = "typeId")
    UserPostResponse toUserPostResponse(ForumPostEntity forumPostEntity);

    List<UserPostResponse> toUserPostResponseList(List<ForumPostEntity> forumPostEntities);
}
