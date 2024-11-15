package com.intellitor.course.controllers;

import com.intellitor.common.dtos.CourseDTO;
import com.intellitor.common.entities.Course;
import com.intellitor.common.utils.ErrorMessages;
import com.intellitor.common.utils.ObjectNames;
import com.intellitor.common.utils.Response;
import com.intellitor.common.utils.BaseTest;
import com.intellitor.course.utils.ObjectMaker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class CourseControllerTest extends BaseTest {

    private final String path = "/course";

    @Autowired
    private ObjectMaker objectMaker;

    @BeforeEach
    public void setUp() {
        objectMaker.courseRepository.deleteAll();
        objectMaker.teacherRepository.deleteAll();
    }

    @Test
    void testCreateCourse_success() throws Exception {
        CourseDTO courseObject = objectMaker.createCourseDTO();
        Response<CourseDTO> response = postForObject(path, courseObject, Response.class, CourseDTO.class);
        assertEquals(200, response.getStatus());
        CourseDTO postedObject = response.getContent();

        final Long courseId = postedObject.getId();
        runInTransaction(status -> {
            Course expectedCourse = objectMaker.courseRepository.findById(courseId).orElseGet(() -> fail("Expected CourseEntity not found"));

            assertEquals(courseId, expectedCourse.getId(), "Course wasn't created successfully");
            return null;
        });
    }

    @Test
    void testCreateCourse_failure() throws Exception {
        CourseDTO courseObject = objectMaker.createCourseDTO();
        Response<CourseDTO> response = postForObject(path, courseObject, Response.class, CourseDTO.class);
        assertEquals(200, response.getStatus());

        Response<String> errResponse = postForError(path, courseObject);
        assertEquals(400, errResponse.getStatus());
        assertEquals(String.format(ErrorMessages.OBJECT_FIELD_ALREADY_EXISTS, ObjectNames.COURSE, "title", courseObject.getTitle()), errResponse.getContent());
    }

    @Test
    void testGetCourseById_success() throws Exception {
        CourseDTO courseObject = objectMaker.createAndSaveCourseDTO();

        Response<CourseDTO> response = getForObject(String.format("%s/%d", path, courseObject.getId()), Response.class, CourseDTO.class);
        assertEquals(200, response.getStatus());
        CourseDTO gotObject = response.getContent();
        assertEquals(courseObject.getId(), gotObject.getId(), "Course wasn't found");
    }

    @Test
    void testGetCourseById_failure() throws Exception {
        Response<String> response = getForError(String.format("%s/%s", path, objectMaker.wrongId));
        assertEquals(400, response.getStatus());
        assertEquals(String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.COURSE, objectMaker.wrongId), response.getContent());
    }

    @Test
    void testUpdateCourse_success() throws Exception {
        CourseDTO courseObject = objectMaker.createAndSaveCourseDTO();

        courseObject.setDescription("updatedDescription");
        Response<CourseDTO> response = putForObject(path, courseObject, Response.class, CourseDTO.class);
        runInTransaction(status -> {
            Course expectedCourse = objectMaker.courseRepository.findById(response.getContent().getId()).orElseGet(() -> fail("Expected CourseEntity not found"));

            assertEquals(courseObject.getDescription(), expectedCourse.getDescription(), "Course description wasn't updated successfully");
            return null;
        });
    }

    @Test
    void testUpdateCourse_failure() throws Exception {
        CourseDTO courseObject = objectMaker.createCourseDTO();
        courseObject.setId(objectMaker.wrongId);
        Response<String> errResponse = putForError(path, courseObject);
        assertEquals(400, errResponse.getStatus());
        assertEquals(String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.COURSE, courseObject.getId()), errResponse.getContent());
    }

    @Test
    void testDeleteCourse_success() throws Exception {
        CourseDTO courseObject = objectMaker.createAndSaveCourseDTO();

        Response<CourseDTO> response = deleteForObject(path, String.valueOf(courseObject.getId()), Response.class, CourseDTO.class);
        assertEquals(200, response.getStatus(), "Course wasn't deleted successfully");
        assertEquals(courseObject.getId(), response.getContent().getId(), "Course wasn't deleted successfully");
    }

    @Test
    void testDeleteCourse_failure() throws Exception {
        Response<String> response = deleteForError(path, String.valueOf(objectMaker.wrongId));
        assertEquals(400, response.getStatus(), "Course was found already");
        assertEquals(String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.COURSE, objectMaker.wrongId), response.getContent());
    }
}