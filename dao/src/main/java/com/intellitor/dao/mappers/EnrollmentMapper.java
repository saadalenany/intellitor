package com.intellitor.dao.mappers;

import com.intellitor.common.dtos.EnrollmentDTO;
import com.intellitor.dao.entities.Enrollment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingInheritanceStrategy;

import java.util.List;

@Mapper(mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_ALL_FROM_CONFIG, componentModel = "spring")
public interface EnrollmentMapper {

    @Mapping(source = "course.id", target = "courseId")
    @Mapping(source = "student.id", target = "studentId")
    EnrollmentDTO toModel(Enrollment entity);
    List<EnrollmentDTO> toModels(List<Enrollment> entities);

    @Mapping(source = "courseId", target = "course.id")
    @Mapping(source = "studentId", target = "student.id")
    Enrollment toEntity(EnrollmentDTO model);
    List<Enrollment> toEntities(List<EnrollmentDTO> models);
}
