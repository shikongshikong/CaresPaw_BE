package com.example.carespawbe.mapper.Auth;
//

import com.example.carespawbe.dto.Auth.LoginResponse;
import com.example.carespawbe.dto.UserProfile.UserInfoResponse;
import com.example.carespawbe.entity.Auth.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    LoginResponse toResponse(UserEntity userEntity);

    UserInfoResponse toUserInfoResponse(UserEntity userEntity);
}
