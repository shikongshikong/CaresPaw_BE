package com.example.carespawbe.mapper;
//

import com.example.carespawbe.dto.Auth.LoginResponse;
import com.example.carespawbe.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    LoginResponse toResponse(User user);
}
