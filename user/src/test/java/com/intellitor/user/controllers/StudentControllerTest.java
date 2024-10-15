package com.intellitor.user.controllers;

import com.intellitor.common.dtos.StudentDTO;
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
class StudentControllerTest {

    @Mock
    private DaoFeignClient daoFeignClient;

    @InjectMocks
    private StudentController studentController;

    @Test
    void testGetStudent() {
        ResponseEntity<Response> response = ResponseEntity.ok(new Response());
        long id = 100L;
        when(daoFeignClient.getStudentById(id)).thenReturn(response);
        studentController.getStudent(id);
        verify(daoFeignClient, atLeastOnce()).getStudentById(id);
    }

    @Test
    void testValidateStudentByEmailAndPassword() {
        ResponseEntity<Response> response = ResponseEntity.ok(new Response());
        String email = "testEmail";
        String password = "testPassword";
        when(daoFeignClient.getStudentByEmailAndPassword(email, password)).thenReturn(response);
        studentController.validateStudentByEmailAndPassword(email, password);
        verify(daoFeignClient, atLeastOnce()).getStudentByEmailAndPassword(email, password);
    }

    @Test
    void testRegisterStudent() {
        ResponseEntity<Response> response = ResponseEntity.ok(new Response());
        StudentDTO studentDTO = createStudentDTO();
        when(daoFeignClient.createStudent(studentDTO)).thenReturn(response);
        studentController.registerStudent(studentDTO);
        verify(daoFeignClient, atLeastOnce()).createStudent(studentDTO);
    }

    @Test
    void testUpdateStudent() {
        ResponseEntity<Response> response = ResponseEntity.ok(new Response());
        StudentDTO studentDTO = createStudentDTO();
        when(daoFeignClient.updateStudent(studentDTO)).thenReturn(response);
        studentController.updateStudent(studentDTO);
        verify(daoFeignClient, atLeastOnce()).updateStudent(studentDTO);
    }

    @Test
    void testDeleteStudent() {
        ResponseEntity<Response> response = ResponseEntity.ok(new Response());
        long id = 100L;
        when(daoFeignClient.deleteStudentById(id)).thenReturn(response);
        studentController.deleteStudent(id);
        verify(daoFeignClient, atLeastOnce()).deleteStudentById(id);
    }

    private StudentDTO createStudentDTO() {
        StudentDTO student = new StudentDTO();
        student.setName("studentName");
        student.setPassword("testPass");
        student.setEmail("testEmail");
        student.setPhone("+20123548474561");
        return student;
    }

}