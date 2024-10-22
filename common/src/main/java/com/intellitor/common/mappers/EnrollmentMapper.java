package com.intellitor.common.mappers;

import com.intellitor.common.dtos.EnrollmentDTO;
import com.intellitor.common.entities.Enrollment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EnrollmentMapper {

    @Mapping(source = "course.id", target = "courseId")
    @Mapping(source = "student.id", target = "studentId")
    @Mapping(source = "quiz.id", target = "quizId")
    EnrollmentDTO toModel(Enrollment entity);
    List<EnrollmentDTO> toModels(List<Enrollment> entities);

    @Mapping(source = "courseId", target = "course.id")
    @Mapping(source = "studentId", target = "student.id")
    @Mapping(source = "quizId", target = "quiz.id")
    Enrollment toEntity(EnrollmentDTO model);
    List<Enrollment> toEntities(List<EnrollmentDTO> models);
}
