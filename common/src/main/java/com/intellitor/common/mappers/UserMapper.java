package com.intellitor.common.mappers;

import com.intellitor.common.dtos.UserDTO;
import com.intellitor.common.entities.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toModel(User entity);
    List<UserDTO> toModels(List<User> entities);

    User toEntity(UserDTO model);
    List<User> toEntities(List<UserDTO> models);
}
