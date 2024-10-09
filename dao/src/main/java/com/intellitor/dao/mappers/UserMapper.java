package com.intellitor.dao.mappers;

import com.intellitor.common.dtos.UserDTO;
import com.intellitor.dao.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingInheritanceStrategy;

import java.util.List;

@Mapper(mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_ALL_FROM_CONFIG, componentModel = "spring")
public interface UserMapper {

    UserDTO toModel(User entity);
    List<UserDTO> toModels(List<User> entities);

    User toEntity(UserDTO model);
    List<User> toEntities(List<UserDTO> models);
}
