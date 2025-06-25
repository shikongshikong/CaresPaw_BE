package com.example.carespawbe.mapper;
//

import com.example.carespawbe.controller.UserResponse;
import com.example.carespawbe.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toResponse(User user);
}
