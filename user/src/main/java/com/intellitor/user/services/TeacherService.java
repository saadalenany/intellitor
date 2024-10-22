package com.intellitor.user.services;

import com.intellitor.common.dtos.TeacherDTO;
import com.intellitor.common.entities.Teacher;
import com.intellitor.common.mappers.TeacherMapper;
import com.intellitor.common.repositories.TeacherRepository;
import com.intellitor.common.utils.ErrorMessages;
import com.intellitor.common.utils.ObjectNames;
import com.intellitor.common.utils.Response;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final TeacherMapper teacherMapper;

    public TeacherService(TeacherRepository teacherRepository, TeacherMapper teacherMapper) {
        this.teacherRepository = teacherRepository;
        this.teacherMapper = teacherMapper;
    }

    public Response findById(Long id) {
        Teacher teacher = teacherRepository.findById(id).orElse(null);
        if (teacher == null) {
            return new Response(400, String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.TEACHER, id));
        }
        return new Response(200, teacherMapper.toModel(teacher));
    }

    public Response findByEmailAndPassword(String email, String password) {
        Teacher teacher = teacherRepository.findByEmailAndPassword(email, password).orElse(null);
        if (teacher == null) {
            return new Response(400, String.format(ErrorMessages.NO_OBJECT_FOUND_BY_EMAIL_PASSWORD, ObjectNames.TEACHER, email, password));
        }
        return new Response(200, teacherMapper.toModel(teacher));
    }

    public Response createTeacher(TeacherDTO teacherDTO) {
        Teacher entity = teacherMapper.toEntity(teacherDTO);
        if (teacherRepository.findByEmailAndPassword(teacherDTO.getEmail(), teacherDTO.getPassword()).isPresent()) {
            return new Response(400, String.format(ErrorMessages.USER_ALREADY_EXISTS_BY_EMAIL_PASSWORD, teacherDTO.getEmail(), teacherDTO.getPassword()));
        }
        Teacher saved = teacherRepository.save(entity);
        return new Response(200, teacherMapper.toModel(saved));
    }

    public Response updateTeacher(TeacherDTO teacherDTO) {
        Teacher entity = teacherMapper.toEntity(teacherDTO);
        if (teacherRepository.findById(teacherDTO.getId()).isEmpty()) {
            return new Response(400, String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.TEACHER, teacherDTO.getId()));
        }
        Teacher saved = teacherRepository.save(entity);
        return new Response(200, teacherMapper.toModel(saved));
    }

    public Response deleteTeacher(Long id) {
        Optional<Teacher> found = teacherRepository.findById(id);
        if (found.isEmpty()) {
            return new Response(400, String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.TEACHER, id));
        }
        teacherRepository.deleteById(id);
        return new Response(200, teacherMapper.toModel(found.get()));
    }

}
