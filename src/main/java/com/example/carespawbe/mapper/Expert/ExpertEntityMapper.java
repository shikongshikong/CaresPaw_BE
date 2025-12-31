package com.example.carespawbe.mapper.Expert;

import com.example.carespawbe.dto.Expert.ExpertApplyRequest;
import com.example.carespawbe.entity.Expert.ExpertEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExpertEntityMapper {

    @Mapping(source = "userId", target = "user.id")
    @Mapping(target = "idImage", ignore = true) // ignore, because these are file
    ExpertEntity toExpertEntity(ExpertApplyRequest expertApplyRequest);
}
