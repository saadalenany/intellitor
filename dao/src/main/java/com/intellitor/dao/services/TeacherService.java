package com.intellitor.dao.services;

import com.intellitor.common.dtos.TeacherDTO;
import com.intellitor.common.utils.ErrorMessages;
import com.intellitor.common.utils.ObjectNames;
import com.intellitor.common.utils.Response;
import com.intellitor.dao.entities.Teacher;
import com.intellitor.dao.mappers.TeacherMapper;
import com.intellitor.dao.repositories.TeacherRepository;
import org.springframework.stereotype.Service;

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
            return new Response(400, String.format(ErrorMessages.NOT_FOUND_BY_ID, ObjectNames.TEACHER, id));
        }
        return new Response(200, teacherMapper.toModel(teacher));
    }

    public Response findByEmailAndPassword(String email, String password) {
        Teacher teacher = teacherRepository.findByEmailAndPassword(email, password).orElse(null);
        if (teacher == null) {
            return new Response(400, String.format(ErrorMessages.NOT_FOUND_BY_EMAIL_PASSWORD, ObjectNames.TEACHER, email, password));
        }
        return new Response(200, teacherMapper.toModel(teacher));
    }

    public Response createTeacher(TeacherDTO teacherDTO) {
        Teacher entity = teacherMapper.toEntity(teacherDTO);
        if (teacherRepository.findByEmailAndPassword(teacherDTO.getEmail(), teacherDTO.getPassword()).isPresent()) {
            return new Response(400, String.format(ErrorMessages.ALREADY_EXISTS_BY_EMAIL_PASSWORD, teacherDTO.getEmail(), teacherDTO.getPassword()));
        }
        Teacher saved = teacherRepository.save(entity);
        return new Response(200, teacherMapper.toModel(saved));
    }
}
