package com.example.carespawbe.mapper;

import com.example.carespawbe.dto.Forum.ForumPostCommentRequest;
import com.example.carespawbe.dto.Forum.ForumPostCommentResponse;
import com.example.carespawbe.entity.ForumPostCommentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ForumPostCommentMapper {
    @Mapping(source = "user.avatar", target = "avatar")
    @Mapping(source = "user.fullname", target = "fullname")
    @Mapping(source = "user.id", target = "userId")
    ForumPostCommentResponse toCommentResponse(ForumPostCommentEntity forumPostCommentEntity);

    List<ForumPostCommentResponse> toCommentResponseList(List<ForumPostCommentEntity> forumPostCommentEntities);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "postId", target = "forumPostEntity.id")
    ForumPostCommentEntity toPostComment(ForumPostCommentRequest forumPostCommentRequest);
}
