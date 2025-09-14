package com.example.carespawbe.mapper;
//

import com.example.carespawbe.dto.Auth.LoginResponse;
import com.example.carespawbe.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    LoginResponse toResponse(UserEntity userEntity);
}
