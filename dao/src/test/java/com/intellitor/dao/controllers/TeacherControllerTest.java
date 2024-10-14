package com.intellitor.dao.controllers;

import com.intellitor.common.dtos.TeacherDTO;
import com.intellitor.common.utils.ErrorMessages;
import com.intellitor.common.utils.ObjectNames;
import com.intellitor.common.utils.Response;
import com.intellitor.dao.entities.Teacher;
import com.intellitor.dao.mappers.TeacherMapper;
import com.intellitor.dao.repositories.TeacherRepository;
import com.intellitor.dao.utils.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class TeacherControllerTest extends BaseTest {

    private final String path = "/teacher";

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private TeacherMapper teacherMapper;

    @BeforeEach
    public void setUp() {
        teacherRepository.deleteAll(); // Clear the database before each test
    }

    @Test
    void testCreateTeacher_success() throws Exception {
        TeacherDTO teacherObject = createTeacherDTO();
        Response<TeacherDTO> response = postForObject(path, teacherObject, Response.class, TeacherDTO.class);
        assertEquals(200, response.getStatus());
        TeacherDTO postedObject = response.getContent();

        final Long teacherId = postedObject.getId();
        runInTransaction(status -> {
            Teacher expectedTeacher = teacherRepository.findById(teacherId).orElseGet(() -> fail("Expected TeacherEntity not found"));

            assertEquals(teacherId, expectedTeacher.getId(), "Teacher wasn't created successfully");
            return null;
        });
    }

    @Test
    void testCreateTeacher_failure() throws Exception {
        TeacherDTO teacherObject = createTeacherDTO();
        Response<TeacherDTO> response = postForObject(path, teacherObject, Response.class, TeacherDTO.class);
        assertEquals(200, response.getStatus());

        Response<String> errResponse = postForObject(path, teacherObject, Response.class, String.class);
        assertEquals(400, errResponse.getStatus());
        assertEquals(String.format(ErrorMessages.USER_ALREADY_EXISTS_BY_EMAIL_PASSWORD, teacherObject.getEmail(), teacherObject.getPassword()), errResponse.getContent());
    }

    @Test
    void testGetTeacherById_success() throws Exception {
        TeacherDTO teacherObject = createTeacherDTO();
        Teacher entity = teacherMapper.toEntity(teacherObject);
        Teacher saved = teacherRepository.save(entity);

        Response<TeacherDTO> response = getForObject(String.format("%s/%d", path, saved.getId()), Response.class, TeacherDTO.class);
        assertEquals(200, response.getStatus());
        TeacherDTO gotObject = response.getContent();
        assertEquals(saved.getId(), gotObject.getId(), "Teacher wasn't found");
    }

    @Test
    void testGetTeacherById_failure() throws Exception {
        final String WRONG_ID = "100";
        Response<String> response = getForObject(String.format("%s/%s", path, WRONG_ID), Response.class, String.class);
        assertEquals(400, response.getStatus());
        assertEquals(String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.TEACHER, WRONG_ID), response.getContent());
    }

    @Test
    void testGetTeacherByEmailAndPassword_success() throws Exception {
        TeacherDTO teacherObject = createTeacherDTO();
        Teacher entity = teacherMapper.toEntity(teacherObject);
        Teacher saved = teacherRepository.save(entity);

        Response<TeacherDTO> response = getForObject(String.format("%s?email=%s&password=%s",path, saved.getEmail(), saved.getPassword()), Response.class, TeacherDTO.class);
        assertEquals(200, response.getStatus());
        TeacherDTO gotObject = response.getContent();
        assertEquals(saved.getId(), gotObject.getId(), "Teacher wasn't found");
        assertEquals(saved.getEmail(), gotObject.getEmail(), "Teacher wasn't found");
        assertEquals(saved.getPassword(), gotObject.getPassword(), "Teacher wasn't found");
    }

    @Test
    void testGetTeacherByEmailAndPassword_failure() throws Exception {
        final String WRONG = "wrong";
        Response<String> response = getForObject(String.format("%s?email=%s&password=%s", path, WRONG, WRONG), Response.class, String.class);
        assertEquals(400, response.getStatus());
        assertEquals(String.format(ErrorMessages.NO_OBJECT_FOUND_BY_EMAIL_PASSWORD, ObjectNames.TEACHER, WRONG, WRONG), response.getContent());
    }

    @Test
    void testUpdateTeacher_success() throws Exception {
        TeacherDTO teacherObject = createTeacherDTO();
        Teacher entity = teacherMapper.toEntity(teacherObject);
        Teacher saved = teacherRepository.save(entity);

        saved.setName("updatedName");
        TeacherDTO model = teacherMapper.toModel(saved);
        Response<TeacherDTO> response = putForObject(path, model, Response.class, TeacherDTO.class);
        runInTransaction(status -> {
            Teacher expectedTeacher = teacherRepository.findById(response.getContent().getId()).orElseGet(() -> fail("Expected TeacherEntity not found"));

            assertEquals(saved.getName(), expectedTeacher.getName(), "Teacher name wasn't updated successfully");
            return null;
        });
    }

    @Test
    void testUpdateTeacher_failure() throws Exception {
        TeacherDTO teacherObject = createTeacherDTO();
        teacherObject.setId(100L);
        Response<String> errResponse = putForObject(path, teacherObject, Response.class, String.class);
        assertEquals(400, errResponse.getStatus());
        assertEquals(String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.TEACHER, teacherObject.getId()), errResponse.getContent());
    }

    @Test
    void testDeleteTeacher_success() throws Exception {
        TeacherDTO teacherObject = createTeacherDTO();
        Teacher entity = teacherMapper.toEntity(teacherObject);
        Teacher saved = teacherRepository.save(entity);

        Response<TeacherDTO> response = deleteForObject(path, String.valueOf(saved.getId()), Response.class, TeacherDTO.class);
        assertEquals(200, response.getStatus(), "Teacher wasn't deleted successfully");
        assertEquals(saved.getId(), response.getContent().getId(), "Teacher wasn't deleted successfully");
    }

    @Test
    void testDeleteTeacher_failure() throws Exception {
        final String WRONG_ID = "100";
        Response<String> response = deleteForObject(path, WRONG_ID, Response.class, String.class);
        assertEquals(400, response.getStatus(), "Teacher was found already");
        assertEquals(String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.TEACHER, WRONG_ID), response.getContent());
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