package com.intellitor.user.services;

import com.intellitor.common.dtos.TeacherDTO;
import com.intellitor.common.entities.Teacher;
import com.intellitor.common.utils.ErrorMessages;
import com.intellitor.common.utils.ObjectNames;
import com.intellitor.common.utils.Response;
import com.intellitor.common.mappers.TeacherMapper;
import com.intellitor.common.config.UserContext;
import com.intellitor.user.repos.TeacherRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final TeacherMapper teacherMapper;
    private final UserContext userContext;

    public TeacherService(TeacherRepository teacherRepository, TeacherMapper teacherMapper, UserContext userContext) {
        this.teacherRepository = teacherRepository;
        this.teacherMapper = teacherMapper;
        this.userContext = userContext;
    }

    public Response findById(Long id) {
        System.out.printf("Logged-in Username:: %s\n", userContext.getUsername());
        Optional<Teacher> byId = teacherRepository.findById(id);
        return byId.map(teacher ->
                new Response(200, teacherMapper.toModel(teacher))).orElseGet(() ->
                new Response(400, String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.TEACHER, id)));
    }

    public Response findByEmailAndPassword(String email, String password) {
        Optional<Teacher> byEmailAndPassword = teacherRepository.findByEmailAndPassword(email, password);
        return byEmailAndPassword.map(teacher ->
                new Response(200, teacherMapper.toModel(teacher))).orElseGet(() ->
                new Response(400, String.format(ErrorMessages.NO_OBJECT_FOUND_BY_EMAIL_PASSWORD, ObjectNames.TEACHER, email, password)));
    }

    public Response createTeacher(TeacherDTO teacherDTO) {
        System.out.printf("Logged-in Username:: %s\n", userContext.getUsername());
        if (teacherRepository.findByEmailAndPassword(teacherDTO.getEmail(), teacherDTO.getPassword()).isPresent()) {
            return new Response(400, String.format(ErrorMessages.USER_ALREADY_EXISTS_BY_EMAIL_PASSWORD, teacherDTO.getEmail(), teacherDTO.getPassword()));
        }
        Teacher entity = teacherMapper.toEntity(teacherDTO);
        entity.setCreatedBy(userContext.getUsername());
        entity.setUpdatedBy(userContext.getUsername());
        Teacher saved = teacherRepository.save(entity);
        return new Response(200, teacherMapper.toModel(saved));
    }

    public Response updateTeacher(TeacherDTO teacherDTO) {
        Optional<Teacher> byId = teacherRepository.findById(teacherDTO.getId());
        if (byId.isEmpty()) {
            return new Response(400, String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.TEACHER, teacherDTO.getId()));
        }
        Teacher entity = teacherMapper.toEntity(teacherDTO);
        entity.setId(byId.get().getId());
        entity.setUpdatedBy(userContext.getUsername());
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
