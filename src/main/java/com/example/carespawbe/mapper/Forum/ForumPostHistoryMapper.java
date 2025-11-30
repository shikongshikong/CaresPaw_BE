package com.example.carespawbe.mapper.Forum;

import com.example.carespawbe.dto.Forum.ForumPostDetailRequest;
import com.example.carespawbe.dto.History.ForumPostSideBarResponse;
import com.example.carespawbe.entity.Forum.ForumPostHistoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ForumPostHistoryMapper {

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "postId", target = "forumPostEntity.id")
    ForumPostHistoryEntity toHistoryEntity(ForumPostDetailRequest forumPostDetailRequest);

    @Mapping(source = "forumPostEntity.title", target = "title")
    @Mapping(source = "forumPostEntity.id", target = "postId")
    @Mapping(source = "forumPostEntity.viewedAmount", target = "viewedAmount")
    @Mapping(source = "forumPostEntity.commentedAmount", target = "commentedAmount")
    @Mapping(source = "forumPostEntity.user.avatar", target = "avatar")
    @Mapping(source = "forumPostEntity.user.id", target = "userId")
    @Mapping(source = "forumPostEntity.user.fullname", target = "fullname")
    ForumPostSideBarResponse toSideBarResponse(ForumPostHistoryEntity forumPostHistoryEntity);

    List<ForumPostSideBarResponse> toHistoryResponseList(List<ForumPostHistoryEntity> forumPostHistories);
}
