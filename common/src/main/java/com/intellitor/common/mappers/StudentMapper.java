package com.intellitor.common.mappers;

import com.intellitor.common.dtos.StudentDTO;
import com.intellitor.common.entities.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(uses = {UserMapper.class, EnrollmentMapper.class}, componentModel = "spring")
public interface StudentMapper {

    @Mapping(source = "enrollments", target = "enrollmentDTOs")
    StudentDTO toModel(Student entity);
    List<StudentDTO> toModels(List<Student> entities);

    @Mapping(source = "enrollmentDTOs", target = "enrollments")
    Student toEntity(StudentDTO model);
    List<Student> toEntities(List<StudentDTO> models);
}
