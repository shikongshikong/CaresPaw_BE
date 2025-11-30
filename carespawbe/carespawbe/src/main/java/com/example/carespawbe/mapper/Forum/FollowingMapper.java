package com.example.carespawbe.mapper.Forum;

import com.example.carespawbe.dto.Follow.FollowingResponse;
import com.example.carespawbe.entity.Forum.FollowingEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FollowingMapper {

    @Mapping(source = "followee.id", target = "userId")
    @Mapping(source = "followee.fullname", target = "fullname")
    @Mapping(source = "followee.avatar", target = "avatar")
    FollowingResponse toFollowingResponse(FollowingEntity followingEntity);

    List<FollowingResponse> toFollowingResponseList(List<FollowingEntity> followingEntities);
}
