package com.intellitor.enrollment.services;

import com.intellitor.common.dtos.EnrollmentDTO;
import com.intellitor.common.entities.Course;
import com.intellitor.common.entities.Enrollment;
import com.intellitor.common.entities.Student;
import com.intellitor.common.mappers.EnrollmentMapper;
import com.intellitor.enrollment.repos.CourseRepository;
import com.intellitor.enrollment.repos.EnrollmentRepository;
import com.intellitor.enrollment.repos.StudentRepository;
import com.intellitor.common.utils.ErrorMessages;
import com.intellitor.common.utils.ObjectNames;
import com.intellitor.common.utils.Response;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentMapper enrollmentMapper;

    public EnrollmentService(EnrollmentRepository enrollmentRepository, StudentRepository studentRepository, CourseRepository courseRepository, EnrollmentMapper enrollmentMapper) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.enrollmentMapper = enrollmentMapper;
    }

    public Response findById(Long id) {
        Optional<Enrollment> byId = enrollmentRepository.findById(id);
        return byId.map(enrollment ->
                new Response(200, enrollmentMapper.toModel(enrollment))).orElseGet(() ->
                new Response(400, String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.ENROLLMENT, id)));
    }

    public Response createEnrollment(EnrollmentDTO enrollmentDTO) {
        Enrollment entity = enrollmentMapper.toEntity(enrollmentDTO);
        return saveEntity(enrollmentDTO, entity);
    }

    public Response updateEnrollment(EnrollmentDTO enrollmentDTO) {
        Optional<Enrollment> byId = enrollmentRepository.findById(enrollmentDTO.getId());
        if (byId.isEmpty()) {
            return new Response(400, String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.ENROLLMENT, enrollmentDTO.getId()));
        }
        Enrollment entity = enrollmentMapper.toEntity(enrollmentDTO);
        entity.setId(byId.get().getId());
        return saveEntity(enrollmentDTO, entity);
    }

    public Response deleteEnrollment(Long id) {
        Optional<Enrollment> found = enrollmentRepository.findById(id);
        if (found.isEmpty()) {
            return new Response(400, String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.ENROLLMENT, id));
        }
        enrollmentRepository.deleteById(id);
        return new Response(200, enrollmentMapper.toModel(found.get()));
    }

    private Response saveEntity(EnrollmentDTO enrollmentDTO, Enrollment entity) {
        Optional<Student> studentOptional = studentRepository.findById(enrollmentDTO.getStudentId());
        if (studentOptional.isEmpty()) {
            return new Response(400, String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.STUDENT, enrollmentDTO.getStudentId()));
        }
        Optional<Course> courseOptional = courseRepository.findById(enrollmentDTO.getCourseId());
        if (courseOptional.isEmpty()) {
            return new Response(400, String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.COURSE, enrollmentDTO.getCourseId()));
        }
        if (enrollmentRepository.findByStudentAndCourse(studentOptional.get(), courseOptional.get()).isPresent()) {
            return new Response(400, String.format(ErrorMessages.OBJECT_FIELD_ALREADY_EXISTS, ObjectNames.ENROLLMENT, "{student, course}", studentOptional.get().getEmail() + " , " + courseOptional.get().getTitle()));
        }
        entity.setStudent(studentOptional.get());
        entity.setCourse(courseOptional.get());
        Enrollment saved = enrollmentRepository.save(entity);
        return new Response(200, enrollmentMapper.toModel(saved));
    }
}
