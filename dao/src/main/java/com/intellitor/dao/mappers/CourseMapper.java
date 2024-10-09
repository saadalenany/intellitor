package com.intellitor.dao.mappers;

import com.intellitor.common.dtos.CourseDTO;
import com.intellitor.dao.entities.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingInheritanceStrategy;

import java.util.List;

@Mapper(uses = EnrollmentMapper.class, mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_ALL_FROM_CONFIG, componentModel = "spring")
public interface CourseMapper {

    @Mapping(source = "teacher.id", target = "teacherId")
    @Mapping(source = "enrollments", target = "enrollmentDTOs")
    CourseDTO toModel(Course entity);
    List<CourseDTO> toModels(List<Course> entities);

    @Mapping(source = "teacherId", target = "teacher.id")
    @Mapping(source = "enrollmentDTOs", target = "enrollments")
    Course toEntity(CourseDTO model);
    List<Course> toEntities(List<CourseDTO> models);
}
