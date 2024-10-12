package com.intellitor.dao.services;

import com.intellitor.common.dtos.StudentDTO;
import com.intellitor.common.utils.ErrorMessages;
import com.intellitor.common.utils.ObjectNames;
import com.intellitor.common.utils.Response;
import com.intellitor.dao.entities.Student;
import com.intellitor.dao.mappers.StudentMapper;
import com.intellitor.dao.repositories.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    public StudentService(StudentRepository studentRepository, StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
    }

    public Response findById(Long id) {
        Student student = studentRepository.findById(id).orElse(null);
        if (student == null) {
            return new Response(400, String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.STUDENT, id));
        }
        return new Response(200, studentMapper.toModel(student));
    }

    public Response findByEmailAndPassword(String email, String password) {
        Student student = studentRepository.findByEmailAndPassword(email, password).orElse(null);
        if (student == null) {
            return new Response(400, String.format(ErrorMessages.NO_OBJECT_FOUND_BY_EMAIL_PASSWORD, ObjectNames.TEACHER, email, password));
        }
        return new Response(200, studentMapper.toModel(student));
    }

    public Response createStudent(StudentDTO studentDTO) {
        Student entity = studentMapper.toEntity(studentDTO);
        if (studentRepository.findByEmailAndPassword(studentDTO.getEmail(), studentDTO.getPassword()).isPresent()) {
            return new Response(400, String.format(ErrorMessages.USER_ALREADY_EXISTS_BY_EMAIL_PASSWORD, studentDTO.getEmail(), studentDTO.getPassword()));
        }
        Student saved = studentRepository.save(entity);
        return new Response(200, studentMapper.toModel(saved));
    }

    public Response updateStudent(StudentDTO studentDTO) {
        Student entity = studentMapper.toEntity(studentDTO);
        if (studentRepository.findById(studentDTO.getId()).isEmpty()) {
            return new Response(400, String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.STUDENT, studentDTO.getId()));
        }
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
