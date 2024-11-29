package com.intellitor.user.services;

import com.intellitor.common.dtos.StudentDTO;
import com.intellitor.common.entities.Student;
import com.intellitor.common.mappers.StudentMapper;
import com.intellitor.common.config.UserContext;
import com.intellitor.user.repos.StudentRepository;
import com.intellitor.common.utils.ErrorMessages;
import com.intellitor.common.utils.ObjectNames;
import com.intellitor.common.utils.Response;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final UserContext userContext;

    public StudentService(StudentRepository studentRepository, StudentMapper studentMapper, UserContext userContext) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
        this.userContext = userContext;
    }

    public Response findById(Long id) {
        Optional<Student> byId = studentRepository.findById(id);
        return byId.map(student ->
                new Response(200, studentMapper.toModel(student))).orElseGet(() ->
                new Response(400, String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.STUDENT, id)));
    }

    public Response findByEmailAndPassword(String email, String password) {
        Optional<Student> byEmailAndPassword = studentRepository.findByEmailAndPassword(email, password);
        return byEmailAndPassword.map(student ->
                new Response(200, studentMapper.toModel(student))).orElseGet(() ->
                new Response(400, String.format(ErrorMessages.NO_OBJECT_FOUND_BY_EMAIL_PASSWORD, ObjectNames.STUDENT, email, password)));
    }

    public Response createStudent(StudentDTO studentDTO) {
        if (studentRepository.findByEmailAndPassword(studentDTO.getEmail(), studentDTO.getPassword()).isPresent()) {
            return new Response(400, String.format(ErrorMessages.USER_ALREADY_EXISTS_BY_EMAIL_PASSWORD, studentDTO.getEmail(), studentDTO.getPassword()));
        }
        Student entity = studentMapper.toEntity(studentDTO);
        entity.setCreatedBy(userContext.getUsername());
        entity.setUpdatedBy(userContext.getUsername());
        Student saved = studentRepository.save(entity);
        return new Response(200, studentMapper.toModel(saved));
    }

    public Response updateStudent(StudentDTO studentDTO) {
        Optional<Student> byId = studentRepository.findById(studentDTO.getId());
        if (byId.isEmpty()) {
            return new Response(400, String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.STUDENT, studentDTO.getId()));
        }
        Student entity = studentMapper.toEntity(studentDTO);
        entity.setId(studentDTO.getId());
        entity.setUpdatedBy(userContext.getUsername());
        Student saved = studentRepository.save(entity);
        return new Response(200, studentMapper.toModel(saved));
    }

    public Response deleteStudent(Long id) {
        Optional<Student> found = studentRepository.findById(id);
        if (found.isEmpty()) {
            return new Response(400, String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.STUDENT, id));
        }
        studentRepository.deleteById(id);
        return new Response(200, studentMapper.toModel(found.get()));
    }

}
