package com.intellitor.enrollment.services;

import com.intellitor.common.dtos.EnrollmentDTO;
import com.intellitor.common.entities.Course;
import com.intellitor.common.entities.Enrollment;
import com.intellitor.common.entities.Student;
import com.intellitor.common.mappers.EnrollmentMapper;
import com.intellitor.common.repositories.CourseRepository;
import com.intellitor.common.repositories.EnrollmentRepository;
import com.intellitor.common.repositories.StudentRepository;
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
        Enrollment enrollment = enrollmentRepository.findById(id).orElse(null);
        if (enrollment == null) {
            return new Response(400, String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.ENROLLMENT, id));
        }
        return new Response(200, enrollmentMapper.toModel(enrollment));
    }

    public Response createEnrollment(EnrollmentDTO enrollmentDTO) {
        Enrollment entity = enrollmentMapper.toEntity(enrollmentDTO);
        return saveEntity(enrollmentDTO, entity);
    }

    public Response updateEnrollment(EnrollmentDTO enrollmentDTO) {
        Enrollment entity = enrollmentMapper.toEntity(enrollmentDTO);
        if (enrollmentRepository.findById(enrollmentDTO.getId()).isEmpty()) {
            return new Response(400, String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.ENROLLMENT, enrollmentDTO.getId()));
        }
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
        Student student = studentRepository.findById(enrollmentDTO.getStudentId()).orElse(null);
        if (student == null) {
            return new Response(400, String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.STUDENT, enrollmentDTO.getStudentId()));
        }
        Course course = courseRepository.findById(enrollmentDTO.getCourseId()).orElse(null);
        if (course == null) {
            return new Response(400, String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.COURSE, enrollmentDTO.getCourseId()));
        }
        if (enrollmentRepository.findByStudentAndCourse(student, course).isPresent()) {
            return new Response(400, String.format(ErrorMessages.OBJECT_FIELD_ALREADY_EXISTS, ObjectNames.ENROLLMENT, "{student, course}", student.getEmail() + " , " + course.getTitle()));
        }
        entity.setStudent(student);
        entity.setCourse(course);
        Enrollment saved = enrollmentRepository.save(entity);
        return new Response(200, enrollmentMapper.toModel(saved));
    }
}
