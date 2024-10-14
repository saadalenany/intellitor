package com.intellitor.dao.controllers;

import com.intellitor.common.dtos.StudentDTO;
import com.intellitor.common.utils.ErrorMessages;
import com.intellitor.common.utils.ObjectNames;
import com.intellitor.common.utils.Response;
import com.intellitor.dao.entities.Student;
import com.intellitor.dao.mappers.StudentMapper;
import com.intellitor.dao.repositories.StudentRepository;
import com.intellitor.dao.utils.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class StudentControllerTest extends BaseTest {

    private final String path = "/student";

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentMapper studentMapper;

    @BeforeEach
    public void setUp() {
        studentRepository.deleteAll(); // Clear the database before each test
    }

    @Test
    void testCreateStudent_success() throws Exception {
        StudentDTO studentObject = createStudentDTO();
        Response<StudentDTO> response = postForObject(path, studentObject, Response.class, StudentDTO.class);
        assertEquals(200, response.getStatus());
        StudentDTO postedObject = response.getContent();

        final Long studentId = postedObject.getId();
        runInTransaction(status -> {
            Student expectedStudent = studentRepository.findById(studentId).orElseGet(() -> fail("Expected StudentEntity not found"));

            assertEquals(studentId, expectedStudent.getId(), "Student wasn't created successfully");
            return null;
        });
    }

    @Test
    void testCreateStudent_failure() throws Exception {
        StudentDTO studentObject = createStudentDTO();
        Response<StudentDTO> response = postForObject(path, studentObject, Response.class, StudentDTO.class);
        assertEquals(200, response.getStatus());

        Response<String> errResponse = postForObject(path, studentObject, Response.class, String.class);
        assertEquals(400, errResponse.getStatus());
        assertEquals(String.format(ErrorMessages.USER_ALREADY_EXISTS_BY_EMAIL_PASSWORD, studentObject.getEmail(), studentObject.getPassword()), errResponse.getContent());
    }

    @Test
    void testGetStudentById_success() throws Exception {
        StudentDTO studentObject = createStudentDTO();
        Student entity = studentMapper.toEntity(studentObject);
        Student saved = studentRepository.save(entity);

        Response<StudentDTO> response = getForObject(String.format("%s/%d", path, saved.getId()), Response.class, StudentDTO.class);
        assertEquals(200, response.getStatus());
        StudentDTO gotObject = response.getContent();
        assertEquals(saved.getId(), gotObject.getId(), "Student wasn't found");
    }

    @Test
    void testGetStudentById_failure() throws Exception {
        final String WRONG_ID = "100";
        Response<String> response = getForObject(String.format("%s/%s", path, WRONG_ID), Response.class, String.class);
        assertEquals(400, response.getStatus());
        assertEquals(String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.STUDENT, WRONG_ID), response.getContent());
    }

    @Test
    void testGetStudentByEmailAndPassword_success() throws Exception {
        StudentDTO studentObject = createStudentDTO();
        Student entity = studentMapper.toEntity(studentObject);
        Student saved = studentRepository.save(entity);

        Response<StudentDTO> response = getForObject(String.format("%s?email=%s&password=%s",path, saved.getEmail(), saved.getPassword()), Response.class, StudentDTO.class);
        assertEquals(200, response.getStatus());
        StudentDTO gotObject = response.getContent();
        assertEquals(saved.getId(), gotObject.getId(), "Student wasn't found");
        assertEquals(saved.getEmail(), gotObject.getEmail(), "Student wasn't found");
        assertEquals(saved.getPassword(), gotObject.getPassword(), "Student wasn't found");
    }

    @Test
    void testGetStudentByEmailAndPassword_failure() throws Exception {
        final String WRONG = "wrong";
        Response<String> response = getForObject(String.format("%s?email=%s&password=%s", path, WRONG, WRONG), Response.class, String.class);
        assertEquals(400, response.getStatus());
        assertEquals(String.format(ErrorMessages.NO_OBJECT_FOUND_BY_EMAIL_PASSWORD, ObjectNames.STUDENT, WRONG, WRONG), response.getContent());
    }

    @Test
    void testUpdateStudent_success() throws Exception {
        StudentDTO studentObject = createStudentDTO();
        Student entity = studentMapper.toEntity(studentObject);
        Student saved = studentRepository.save(entity);

        saved.setName("updatedName");
        StudentDTO model = studentMapper.toModel(saved);
        Response<StudentDTO> response = putForObject(path, model, Response.class, StudentDTO.class);
        runInTransaction(status -> {
            Student expectedStudent = studentRepository.findById(response.getContent().getId()).orElseGet(() -> fail("Expected StudentEntity not found"));

            assertEquals(saved.getName(), expectedStudent.getName(), "Student name wasn't updated successfully");
            return null;
        });
    }

    @Test
    void testUpdateStudent_failure() throws Exception {
        StudentDTO studentObject = createStudentDTO();
        studentObject.setId(100L);
        Response<String> errResponse = putForObject(path, studentObject, Response.class, String.class);
        assertEquals(400, errResponse.getStatus());
        assertEquals(String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.STUDENT, studentObject.getId()), errResponse.getContent());
    }

    @Test
    void testDeleteStudent_success() throws Exception {
        StudentDTO studentObject = createStudentDTO();
        Student entity = studentMapper.toEntity(studentObject);
        Student saved = studentRepository.save(entity);

        Response<StudentDTO> response = deleteForObject(path, String.valueOf(saved.getId()), Response.class, StudentDTO.class);
        assertEquals(200, response.getStatus(), "Student wasn't deleted successfully");
        assertEquals(saved.getId(), response.getContent().getId(), "Student wasn't deleted successfully");
    }

    @Test
    void testDeleteStudent_failure() throws Exception {
        final String WRONG_ID = "100";
        Response<String> response = deleteForObject(path, WRONG_ID, Response.class, String.class);
        assertEquals(400, response.getStatus(), "Student was found already");
        assertEquals(String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.STUDENT, WRONG_ID), response.getContent());
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