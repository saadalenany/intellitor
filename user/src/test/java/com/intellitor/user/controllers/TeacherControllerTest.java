package com.intellitor.user.controllers;

import com.intellitor.common.dtos.TeacherDTO;
import com.intellitor.common.utils.ErrorMessages;
import com.intellitor.common.utils.ObjectNames;
import com.intellitor.common.utils.Response;
import com.intellitor.common.entities.Teacher;
import com.intellitor.common.utils.BaseTest;
import com.intellitor.common.utils.ObjectMaker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class TeacherControllerTest extends BaseTest {

    private final String path = "/teacher";

    @Autowired
    private ObjectMaker objectMaker;

    @BeforeEach
    public void setUp() {
        objectMaker.teacherRepository.deleteAll();
    }

    @Test
    void testCreateTeacher_success() throws Exception {
        TeacherDTO teacherDTO = objectMaker.createTeacherDTO();
        Response<TeacherDTO> response = postForObject(path, teacherDTO, Response.class, TeacherDTO.class);
        assertEquals(200, response.getStatus());
        TeacherDTO postedObject = response.getContent();

        final Long teacherId = postedObject.getId();
        runInTransaction(status -> {
            Teacher expectedTeacher = objectMaker.teacherRepository.findById(teacherId).orElseGet(() -> fail("Expected TeacherEntity not found"));

            assertEquals(teacherId, expectedTeacher.getId(), "Teacher wasn't created successfully");
            return null;
        });
    }

    @Test
    void testCreateTeacher_failure() throws Exception {
        TeacherDTO teacherDTO = objectMaker.createTeacherDTO();
        Response<TeacherDTO> response = postForObject(path, teacherDTO, Response.class, TeacherDTO.class);
        assertEquals(200, response.getStatus());

        Response<String> errResponse = postForError(path, teacherDTO);
        assertEquals(400, errResponse.getStatus());
        assertEquals(String.format(ErrorMessages.USER_ALREADY_EXISTS_BY_EMAIL_PASSWORD, teacherDTO.getEmail(), teacherDTO.getPassword()), errResponse.getContent());
    }

    @Test
    void testGetTeacherById_success() throws Exception {
        TeacherDTO teacherDTO = objectMaker.createAndSaveTeacherDTO();

        Response<TeacherDTO> response = getForObject(String.format("%s/%d", path, teacherDTO.getId()), Response.class, TeacherDTO.class);
        assertEquals(200, response.getStatus());
        TeacherDTO gotObject = response.getContent();
        assertEquals(teacherDTO.getId(), gotObject.getId(), "Teacher wasn't found");
    }

    @Test
    void testGetTeacherById_failure() throws Exception {
        Response<String> response = getForError(String.format("%s/%s", path, objectMaker.wrongId));
        assertEquals(400, response.getStatus());
        assertEquals(String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.TEACHER, objectMaker.wrongId), response.getContent());
    }

    @Test
    void testGetTeacherByEmailAndPassword_success() throws Exception {
        TeacherDTO teacherDTO = objectMaker.createAndSaveTeacherDTO();

        Response<TeacherDTO> response = getForObject(String.format("%s?email=%s&password=%s",path, teacherDTO.getEmail(), teacherDTO.getPassword()), Response.class, TeacherDTO.class);
        assertEquals(200, response.getStatus());
        TeacherDTO gotObject = response.getContent();
        assertEquals(teacherDTO.getId(), gotObject.getId(), "Teacher wasn't found");
        assertEquals(teacherDTO.getEmail(), gotObject.getEmail(), "Teacher wasn't found");
        assertEquals(teacherDTO.getPassword(), gotObject.getPassword(), "Teacher wasn't found");
    }

    @Test
    void testGetTeacherByEmailAndPassword_failure() throws Exception {
        Response<String> response = getForError(String.format("%s?email=%s&password=%s", path, objectMaker.wrongValue, objectMaker.wrongValue));
        assertEquals(400, response.getStatus());
        assertEquals(String.format(ErrorMessages.NO_OBJECT_FOUND_BY_EMAIL_PASSWORD, ObjectNames.TEACHER, objectMaker.wrongValue, objectMaker.wrongValue), response.getContent());
    }

    @Test
    void testUpdateTeacher_success() throws Exception {
        TeacherDTO teacherDTO = objectMaker.createAndSaveTeacherDTO();

        teacherDTO.setName("updatedName");
        Response<TeacherDTO> response = putForObject(path, teacherDTO, Response.class, TeacherDTO.class);
        runInTransaction(status -> {
            Teacher expectedTeacher = objectMaker.teacherRepository.findById(response.getContent().getId()).orElseGet(() -> fail("Expected TeacherEntity not found"));

            assertEquals(teacherDTO.getName(), expectedTeacher.getName(), "Teacher name wasn't updated successfully");
            return null;
        });
    }

    @Test
    void testUpdateTeacher_failure() throws Exception {
        TeacherDTO teacherDTO = objectMaker.createTeacherDTO();
        teacherDTO.setId(objectMaker.wrongId);
        Response<String> errResponse = putForError(path, teacherDTO);
        assertEquals(400, errResponse.getStatus());
        assertEquals(String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.TEACHER, teacherDTO.getId()), errResponse.getContent());
    }

    @Test
    void testDeleteTeacher_success() throws Exception {
        TeacherDTO teacherDTO = objectMaker.createAndSaveTeacherDTO();

        Response<TeacherDTO> response = deleteForObject(path, String.valueOf(teacherDTO.getId()), Response.class, TeacherDTO.class);
        assertEquals(200, response.getStatus(), "Teacher wasn't deleted successfully");
        assertEquals(teacherDTO.getId(), response.getContent().getId(), "Teacher wasn't deleted successfully");
    }

    @Test
    void testDeleteTeacher_failure() throws Exception {
        Response<String> response = deleteForError(path, String.valueOf(objectMaker.wrongId));
        assertEquals(400, response.getStatus(), "Teacher was found already");
        assertEquals(String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.TEACHER, objectMaker.wrongId), response.getContent());
    }

}