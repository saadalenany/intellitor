package com.intellitor.dao.controllers;

import com.intellitor.common.dtos.StudentDTO;
import com.intellitor.common.utils.ErrorMessages;
import com.intellitor.common.utils.ObjectNames;
import com.intellitor.common.utils.Response;
import com.intellitor.dao.entities.Student;
import com.intellitor.dao.utils.BaseTest;
import com.intellitor.dao.utils.ObjectMaker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class StudentControllerTest extends BaseTest {

    private final String path = "/student";

    @Autowired
    private ObjectMaker objectMaker;

    @BeforeEach
    public void setUp() {
        objectMaker.studentRepository.deleteAll();
    }

    @Test
    void testCreateStudent_success() throws Exception {
        StudentDTO studentDTO = objectMaker.createStudentDTO();
        Response<StudentDTO> response = postForObject(path, studentDTO, Response.class, StudentDTO.class);
        assertEquals(200, response.getStatus());
        StudentDTO postedObject = response.getContent();

        final Long studentId = postedObject.getId();
        runInTransaction(status -> {
            Student expectedStudent = objectMaker.studentRepository.findById(studentId).orElseGet(() -> fail("Expected StudentEntity not found"));

            assertEquals(studentId, expectedStudent.getId(), "Student wasn't created successfully");
            return null;
        });
    }

    @Test
    void testCreateStudent_failure() throws Exception {
        StudentDTO studentDTO = objectMaker.createStudentDTO();
        Response<StudentDTO> response = postForObject(path, studentDTO, Response.class, StudentDTO.class);
        assertEquals(200, response.getStatus());

        Response<String> errResponse = postForError(path, studentDTO);
        assertEquals(400, errResponse.getStatus());
        assertEquals(String.format(ErrorMessages.USER_ALREADY_EXISTS_BY_EMAIL_PASSWORD, studentDTO.getEmail(), studentDTO.getPassword()), errResponse.getContent());
    }

    @Test
    void testGetStudentById_success() throws Exception {
        StudentDTO studentDTO = objectMaker.createAndSaveStudentDTO();

        Response<StudentDTO> response = getForObject(String.format("%s/%d", path, studentDTO.getId()), Response.class, StudentDTO.class);
        assertEquals(200, response.getStatus());
        StudentDTO gotObject = response.getContent();
        assertEquals(studentDTO.getId(), gotObject.getId(), "Student wasn't found");
    }

    @Test
    void testGetStudentById_failure() throws Exception {
        Response<String> response = getForError(String.format("%s/%s", path, objectMaker.wrongId));
        assertEquals(400, response.getStatus());
        assertEquals(String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.STUDENT, objectMaker.wrongId), response.getContent());
    }

    @Test
    void testGetStudentByEmailAndPassword_success() throws Exception {
        StudentDTO studentDTO = objectMaker.createAndSaveStudentDTO();

        Response<StudentDTO> response = getForObject(String.format("%s?email=%s&password=%s",path, studentDTO.getEmail(), studentDTO.getPassword()), Response.class, StudentDTO.class);
        assertEquals(200, response.getStatus());
        StudentDTO gotObject = response.getContent();
        assertEquals(studentDTO.getId(), gotObject.getId(), "Student wasn't found");
        assertEquals(studentDTO.getEmail(), gotObject.getEmail(), "Student wasn't found");
        assertEquals(studentDTO.getPassword(), gotObject.getPassword(), "Student wasn't found");
    }

    @Test
    void testGetStudentByEmailAndPassword_failure() throws Exception {
        Response<String> response = getForError(String.format("%s?email=%s&password=%s", path, objectMaker.wrongValue, objectMaker.wrongValue));
        assertEquals(400, response.getStatus());
        assertEquals(String.format(ErrorMessages.NO_OBJECT_FOUND_BY_EMAIL_PASSWORD, ObjectNames.STUDENT, objectMaker.wrongValue, objectMaker.wrongValue), response.getContent());
    }

    @Test
    void testUpdateStudent_success() throws Exception {
        StudentDTO studentDTO = objectMaker.createAndSaveStudentDTO();

        studentDTO.setName("updatedName");
        Response<StudentDTO> response = putForObject(path, studentDTO, Response.class, StudentDTO.class);
        runInTransaction(status -> {
            Student expectedStudent = objectMaker.studentRepository.findById(response.getContent().getId()).orElseGet(() -> fail("Expected StudentEntity not found"));

            assertEquals(studentDTO.getName(), expectedStudent.getName(), "Student name wasn't updated successfully");
            return null;
        });
    }

    @Test
    void testUpdateStudent_failure() throws Exception {
        StudentDTO studentDTO = objectMaker.createStudentDTO();
        studentDTO.setId(objectMaker.wrongId);
        Response<String> errResponse = putForError(path, studentDTO);
        assertEquals(400, errResponse.getStatus());
        assertEquals(String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.STUDENT, studentDTO.getId()), errResponse.getContent());
    }

    @Test
    void testDeleteStudent_success() throws Exception {
        StudentDTO studentDTO = objectMaker.createAndSaveStudentDTO();

        Response<StudentDTO> response = deleteForObject(path, String.valueOf(studentDTO.getId()), Response.class, StudentDTO.class);
        assertEquals(200, response.getStatus(), "Student wasn't deleted successfully");
        assertEquals(studentDTO.getId(), response.getContent().getId(), "Student wasn't deleted successfully");
    }

    @Test
    void testDeleteStudent_failure() throws Exception {
        Response<String> response = deleteForError(path, String.valueOf(objectMaker.wrongId));
        assertEquals(400, response.getStatus(), "Student was found already");
        assertEquals(String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.STUDENT, objectMaker.wrongId), response.getContent());
    }
}