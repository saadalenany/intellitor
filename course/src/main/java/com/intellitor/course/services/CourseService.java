package com.intellitor.course.services;

import com.intellitor.common.dtos.CourseDTO;
import com.intellitor.common.entities.Course;
import com.intellitor.common.mappers.CourseMapper;
import com.intellitor.common.repositories.CourseRepository;
import com.intellitor.common.utils.ErrorMessages;
import com.intellitor.common.utils.ObjectNames;
import com.intellitor.common.utils.Response;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    public CourseService(CourseRepository courseRepository, CourseMapper courseMapper) {
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
    }

    public Response findById(Long id) {
        Optional<Course> byId = courseRepository.findById(id);
        return byId.map(course ->
                new Response(200, courseMapper.toModel(course))).orElseGet(() ->
                new Response(400, String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.COURSE, id)));
    }

    public Response createCourse(CourseDTO courseDTO) {
        Optional<Course> byTitle = courseRepository.findByTitle(courseDTO.getTitle());
        if (byTitle.isPresent()) {
            return new Response(400, String.format(ErrorMessages.OBJECT_FIELD_ALREADY_EXISTS, ObjectNames.COURSE, "title", courseDTO.getTitle()));
        }
        Course saved = courseRepository.save(courseMapper.toEntity(courseDTO));
        return new Response(200, courseMapper.toModel(saved));
    }

    public Response updateCourse(CourseDTO courseDTO) {
        Optional<Course> byId = courseRepository.findById(courseDTO.getId());
        if (byId.isEmpty()) {
            return new Response(400, String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.COURSE, courseDTO.getId()));
        }
        Course entity = courseMapper.toEntity(courseDTO);
        entity.setId(byId.get().getId());
        Course saved = courseRepository.save(entity);
        return new Response(200, courseMapper.toModel(saved));
    }

    public Response deleteCourse(Long id) {
        Optional<Course> found = courseRepository.findById(id);
        if (found.isEmpty()) {
            return new Response(400, String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.COURSE, id));
        }
        courseRepository.deleteById(id);
        return new Response(200, courseMapper.toModel(found.get()));
    }
}
