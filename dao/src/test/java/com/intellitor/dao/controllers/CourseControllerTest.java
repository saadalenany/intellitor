package com.intellitor.dao.controllers;

import com.intellitor.common.dtos.CourseDTO;
import com.intellitor.common.utils.ErrorMessages;
import com.intellitor.common.utils.ObjectNames;
import com.intellitor.common.utils.Response;
import com.intellitor.dao.entities.Course;
import com.intellitor.dao.entities.Teacher;
import com.intellitor.dao.mappers.CourseMapper;
import com.intellitor.dao.repositories.CourseRepository;
import com.intellitor.dao.repositories.TeacherRepository;
import com.intellitor.dao.utils.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class CourseControllerTest extends BaseTest {

    private final String path = "/course";

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseMapper courseMapper;

    @BeforeEach
    public void setUp() {
        // Clear the database before each test
        courseRepository.deleteAll();
        teacherRepository.deleteAll();
    }

    @Test
    void testCreateCourse_success() throws Exception {
        CourseDTO courseObject = createCourseDTO();
        Response<CourseDTO> response = postForObject(path, courseObject, Response.class, CourseDTO.class);
        assertEquals(200, response.getStatus());
        CourseDTO postedObject = response.getContent();

        final Long courseId = postedObject.getId();
        runInTransaction(status -> {
            Course expectedCourse = courseRepository.findById(courseId).orElseGet(() -> fail("Expected CourseEntity not found"));

            assertEquals(courseId, expectedCourse.getId(), "Course wasn't created successfully");
            return null;
        });
    }

    @Test
    void testCreateCourse_failure() throws Exception {
        CourseDTO courseObject = createCourseDTO();
        Response<CourseDTO> response = postForObject(path, courseObject, Response.class, CourseDTO.class);
        assertEquals(200, response.getStatus());

        Response<String> errResponse = postForObject(path, courseObject, Response.class, String.class);
        assertEquals(400, errResponse.getStatus());
        assertEquals(String.format(ErrorMessages.OBJECT_FIELD_ALREADY_EXISTS, ObjectNames.COURSE, "title", courseObject.getTitle()), errResponse.getContent());
    }

    @Test
    void testGetCourseById_success() throws Exception {
        CourseDTO courseObject = createCourseDTO();
        Course entity = courseMapper.toEntity(courseObject);
        Course saved = courseRepository.save(entity);

        Response<CourseDTO> response = getForObject(String.format("%s/%d", path, saved.getId()), Response.class, CourseDTO.class);
        assertEquals(200, response.getStatus());
        CourseDTO gotObject = response.getContent();
        assertEquals(saved.getId(), gotObject.getId(), "Course wasn't found");
    }

    @Test
    void testGetCourseById_failure() throws Exception {
        final String WRONG_ID = "100";
        Response<String> response = getForObject(String.format("%s/%s", path, WRONG_ID), Response.class, String.class);
        assertEquals(400, response.getStatus());
        assertEquals(String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.COURSE, WRONG_ID), response.getContent());
    }

    @Test
    void testUpdateCourse_success() throws Exception {
        CourseDTO courseObject = createCourseDTO();
        Course entity = courseMapper.toEntity(courseObject);
        Course saved = courseRepository.save(entity);

        saved.setDescription("updatedDescription");
        CourseDTO model = courseMapper.toModel(saved);
        Response<CourseDTO> response = putForObject(path, model, Response.class, CourseDTO.class);
        runInTransaction(status -> {
            Course expectedCourse = courseRepository.findById(response.getContent().getId()).orElseGet(() -> fail("Expected CourseEntity not found"));

            assertEquals(saved.getDescription(), expectedCourse.getDescription(), "Course description wasn't updated successfully");
            return null;
        });
    }

    @Test
    void testUpdateCourse_failure() throws Exception {
        CourseDTO courseObject = createCourseDTO();
        courseObject.setId(100L);
        Response<String> errResponse = putForObject(path, courseObject, Response.class, String.class);
        assertEquals(400, errResponse.getStatus());
        assertEquals(String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.COURSE, courseObject.getId()), errResponse.getContent());
    }

    @Test
    void testDeleteCourse_success() throws Exception {
        CourseDTO courseObject = createCourseDTO();
        Course entity = courseMapper.toEntity(courseObject);
        Course saved = courseRepository.save(entity);

        Response<CourseDTO> response = deleteForObject(path, String.valueOf(saved.getId()), Response.class, CourseDTO.class);
        assertEquals(200, response.getStatus(), "Course wasn't deleted successfully");
        assertEquals(saved.getId(), response.getContent().getId(), "Course wasn't deleted successfully");
    }

    @Test
    void testDeleteCourse_failure() throws Exception {
        final String WRONG_ID = "100";
        Response<String> response = deleteForObject(path, WRONG_ID, Response.class, String.class);
        assertEquals(400, response.getStatus(), "Course was found already");
        assertEquals(String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.COURSE, WRONG_ID), response.getContent());
    }

    private CourseDTO createCourseDTO() {
        CourseDTO course = new CourseDTO();
        course.setTitle("courseTitle");
        course.setDescription("testDescription");
        course.setTeacherId(createTeacherEntity().getId());
        return course;
    }

    private Teacher createTeacherEntity() {
        Teacher teacher = new Teacher();
        teacher.setName("teacherName");
        teacher.setPassword("testPass");
        teacher.setEmail("testEmail");
        teacher.setPhone("+20123548474561");
        return teacherRepository.save(teacher);
    }
}