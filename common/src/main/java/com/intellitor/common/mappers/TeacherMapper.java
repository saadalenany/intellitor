package com.intellitor.common.mappers;

import com.intellitor.common.dtos.TeacherDTO;
import com.intellitor.common.entities.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(uses = {UserMapper.class, CourseMapper.class}, componentModel = "spring")
public interface TeacherMapper {

    @Mapping(source = "courses", target = "courseDTOs")
    TeacherDTO toModel(Teacher entity);

    List<TeacherDTO> toModels(List<Teacher> entities);

    @Mapping(source = "courseDTOs", target = "courses")
    Teacher toEntity(TeacherDTO model);

    List<Teacher> toEntities(List<TeacherDTO> models);
}
