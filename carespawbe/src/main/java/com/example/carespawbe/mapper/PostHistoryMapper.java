package com.example.carespawbe.mapper;

import com.example.carespawbe.dto.Forum.PostDetailRequest;
import com.example.carespawbe.dto.History.PostSideBarResponse;
import com.example.carespawbe.entity.PostHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostHistoryMapper {

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "postId", target = "post.id")
    PostHistory toHistoryEntity(PostDetailRequest postDetailRequest);

    @Mapping(source = "post.title", target = "title")
    @Mapping(source = "post.id", target = "postId")
    @Mapping(source = "post.viewedAmount", target = "viewedAmount")
    @Mapping(source = "post.commentedAmount", target = "commentedAmount")
    @Mapping(source = "post.user.avatar", target = "avatar")
    @Mapping(source = "post.user.id", target = "userId")
    @Mapping(source = "post.user.fullname", target = "fullname")
    PostSideBarResponse toSideBarResponse(PostHistory postHistory);

    List<PostSideBarResponse> toHistoryResponseList(List<PostHistory> forumPostHistories);
}
