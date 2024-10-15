package com.intellitor.user.controllers;

import com.intellitor.common.dtos.TeacherDTO;
import com.intellitor.common.utils.Response;
import com.intellitor.user.feign.DaoFeignClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class TeacherControllerTest {

    @Mock
    private DaoFeignClient daoFeignClient;

    @InjectMocks
    private TeacherController teacherController;

    @Test
    void testGetTeacher() {
        ResponseEntity<Response> response = ResponseEntity.ok(new Response());
        long id = 100L;
        when(daoFeignClient.getTeacherById(id)).thenReturn(response);
        teacherController.getTeacher(id);
        verify(daoFeignClient, atLeastOnce()).getTeacherById(id);
    }

    @Test
    void testValidateTeacherByEmailAndPassword() {
        ResponseEntity<Response> response = ResponseEntity.ok(new Response());
        String email = "testEmail";
        String password = "testPassword";
        when(daoFeignClient.getTeacherByEmailAndPassword(email, password)).thenReturn(response);
        teacherController.validateTeacherByEmailAndPassword(email, password);
        verify(daoFeignClient, atLeastOnce()).getTeacherByEmailAndPassword(email, password);
    }

    @Test
    void testRegisterTeacher() {
        ResponseEntity<Response> response = ResponseEntity.ok(new Response());
        TeacherDTO teacherDTO = createTeacherDTO();
        when(daoFeignClient.createTeacher(teacherDTO)).thenReturn(response);
        teacherController.registerTeacher(teacherDTO);
        verify(daoFeignClient, atLeastOnce()).createTeacher(teacherDTO);
    }

    @Test
    void testUpdateTeacher() {
        ResponseEntity<Response> response = ResponseEntity.ok(new Response());
        TeacherDTO teacherDTO = createTeacherDTO();
        when(daoFeignClient.updateTeacher(teacherDTO)).thenReturn(response);
        teacherController.updateTeacher(teacherDTO);
        verify(daoFeignClient, atLeastOnce()).updateTeacher(teacherDTO);
    }

    @Test
    void testDeleteTeacher() {
        ResponseEntity<Response> response = ResponseEntity.ok(new Response());
        long id = 100L;
        when(daoFeignClient.deleteTeacherById(id)).thenReturn(response);
        teacherController.deleteTeacher(id);
        verify(daoFeignClient, atLeastOnce()).deleteTeacherById(id);
    }

    private TeacherDTO createTeacherDTO() {
        TeacherDTO teacher = new TeacherDTO();
        teacher.setName("teacherName");
        teacher.setPassword("testPass");
        teacher.setEmail("testEmail");
        teacher.setPhone("+20123548474561");
        return teacher;
    }

}