package com.intellitor.dao.controllers;

import com.intellitor.common.dtos.CourseDTO;
import com.intellitor.common.dtos.EnrollmentDTO;
import com.intellitor.common.dtos.StudentDTO;
import com.intellitor.common.utils.ErrorMessages;
import com.intellitor.common.utils.ObjectNames;
import com.intellitor.common.utils.Response;
import com.intellitor.dao.entities.Course;
import com.intellitor.dao.entities.Enrollment;
import com.intellitor.dao.entities.Student;
import com.intellitor.dao.entities.Teacher;
import com.intellitor.dao.mappers.CourseMapper;
import com.intellitor.dao.mappers.EnrollmentMapper;
import com.intellitor.dao.mappers.StudentMapper;
import com.intellitor.dao.repositories.CourseRepository;
import com.intellitor.dao.repositories.EnrollmentRepository;
import com.intellitor.dao.repositories.StudentRepository;
import com.intellitor.dao.repositories.TeacherRepository;
import com.intellitor.dao.utils.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class EnrollmentControllerTest extends BaseTest {

    private final String path = "/enrollment";
    private final Random random = new Random();

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private EnrollmentMapper enrollmentMapper;

    @BeforeEach
    public void setUp() {
        // Clear the database before each test
        enrollmentRepository.deleteAll();
        studentRepository.deleteAll();
        courseRepository.deleteAll();
        teacherRepository.deleteAll();
    }

    @Test
    void testCreateEnrollment_success() throws Exception {
        EnrollmentDTO enrollmentObject = createEnrollmentObject();
        Response<EnrollmentDTO> response = postForObject(path, enrollmentObject, Response.class, EnrollmentDTO.class);
        assertEquals(200, response.getStatus());
        EnrollmentDTO postedObject = response.getContent();

        final Long enrollmentId = postedObject.getId();
        runInTransaction(status -> {
            Enrollment expectedEnrollment = enrollmentRepository.findById(enrollmentId).orElseGet(() -> fail("Expected EnrollmentEntity not found"));

            assertEquals(enrollmentId, expectedEnrollment.getId(), "Enrollment wasn't created successfully");
            return null;
        });
    }

    @Test
    void testCreateEnrollment_failureWrongStudent() throws Exception {
        final Long WRONG_ID = 100L;
        EnrollmentDTO enrollmentObject = new EnrollmentDTO();

        enrollmentObject.setStudentId(WRONG_ID);
        Response<String> response = postForObject(path, enrollmentObject, Response.class, String.class);
        assertEquals(400, response.getStatus());
        assertEquals(String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.STUDENT, WRONG_ID), response.getContent());
    }

    @Test
    void testCreateEnrollment_failureWrongCourse() throws Exception {
        final Long WRONG_ID = 100L;
        EnrollmentDTO enrollmentObject = new EnrollmentDTO();

        enrollmentObject.setStudentId(createStudentObject().getId());
        enrollmentObject.setCourseId(WRONG_ID);
        Response<String> response = postForObject(path, enrollmentObject, Response.class, String.class);
        assertEquals(400, response.getStatus());
        assertEquals(String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.COURSE, WRONG_ID), response.getContent());
    }

    @Test
    void testCreateEnrollment_failureDuplicateStudentAndCourse() throws Exception {
        EnrollmentDTO enrollmentObject = createEnrollmentObject();
        Response<EnrollmentDTO> successResponse = postForObject(path, enrollmentObject, Response.class, EnrollmentDTO.class);
        assertEquals(200, successResponse.getStatus());

        Response<String> response = postForObject(path, enrollmentObject, Response.class, String.class);
        assertEquals(400, response.getStatus());

        runInTransaction(status -> {
            Course course = courseRepository.findById(successResponse.getContent().getCourseId()).orElse(null);
            Student student = studentRepository.findById(successResponse.getContent().getStudentId()).orElse(null);
            assertEquals(
                    String.format(ErrorMessages.OBJECT_FIELD_ALREADY_EXISTS, ObjectNames.ENROLLMENT, "{student, course}", student.getEmail()+" , "+course.getTitle()),
                    response.getContent()
            );
            return null;
        });
    }

    @Test
    void testGetEnrollmentById_success() throws Exception {
        EnrollmentDTO enrollmentObject = createEnrollmentObject();
        Enrollment entity = enrollmentMapper.toEntity(enrollmentObject);
        Enrollment saved = enrollmentRepository.save(entity);

        Response<EnrollmentDTO> response = getForObject(String.format("%s/%d", path, saved.getId()), Response.class, EnrollmentDTO.class);
        assertEquals(200, response.getStatus());
        EnrollmentDTO gotObject = response.getContent();
        assertEquals(saved.getId(), gotObject.getId(), "Enrollment wasn't found");
    }

    @Test
    void testGetEnrollmentById_failure() throws Exception {
        final String WRONG_ID = "100";
        Response<String> response = getForObject(String.format("%s/%s", path, WRONG_ID), Response.class, String.class);
        assertEquals(400, response.getStatus());
        assertEquals(String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.ENROLLMENT, WRONG_ID), response.getContent());
    }

    @Test
    void testUpdateEnrollment_success() throws Exception {
        EnrollmentDTO enrollmentObject = createEnrollmentObject();
        Enrollment entity = enrollmentMapper.toEntity(enrollmentObject);
        Enrollment saved = enrollmentRepository.save(entity);
        EnrollmentDTO model = enrollmentMapper.toModel(saved);

        StudentDTO newStudent = createStudentObject();
        model.setStudentId(newStudent.getId());
        Response<EnrollmentDTO> response = putForObject(path, model, Response.class, EnrollmentDTO.class);
        runInTransaction(status -> {
            Enrollment expectedEnrollment = enrollmentRepository.findById(response.getContent().getId()).orElseGet(() -> fail("Expected EnrollmentEntity not found"));

            assertEquals(newStudent.getEmail(), expectedEnrollment.getStudent().getEmail(), "Enrollment description wasn't updated successfully");
            return null;
        });
    }

    @Test
    void testUpdateEnrollment_failure() throws Exception {
        EnrollmentDTO enrollmentObject = createEnrollmentObject();
        enrollmentObject.setId(100L);
        Response<String> errResponse = putForObject(path, enrollmentObject, Response.class, String.class);
        assertEquals(400, errResponse.getStatus());
        assertEquals(String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.ENROLLMENT, enrollmentObject.getId()), errResponse.getContent());
    }

    @Test
    void testDeleteEnrollment_success() throws Exception {
        EnrollmentDTO enrollmentObject = createEnrollmentObject();
        Enrollment entity = enrollmentMapper.toEntity(enrollmentObject);
        Enrollment saved = enrollmentRepository.save(entity);

        Response<EnrollmentDTO> response = deleteForObject(path, String.valueOf(saved.getId()), Response.class, EnrollmentDTO.class);
        assertEquals(200, response.getStatus(), "Enrollment wasn't deleted successfully");
        assertEquals(saved.getId(), response.getContent().getId(), "Enrollment wasn't deleted successfully");
    }

    @Test
    void testDeleteEnrollment_failure() throws Exception {
        final String WRONG_ID = "100";
        Response<String> response = deleteForObject(path, WRONG_ID, Response.class, String.class);
        assertEquals(400, response.getStatus(), "Enrollment was found already");
        assertEquals(String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.ENROLLMENT, WRONG_ID), response.getContent());
    }

    private EnrollmentDTO createEnrollmentObject() {
        EnrollmentDTO enrollment = new EnrollmentDTO();
        enrollment.setCourseId(createCourseObject().getId());
        enrollment.setStudentId(createStudentObject().getId());
        return enrollment;
    }

    private StudentDTO createStudentObject() {
        Student student = new Student();
        student.setName("studentName");
        student.setPassword("testPass");
        student.setEmail("testEmail" + random.nextInt(1000));
        student.setPhone("+20123548474561");
        return studentMapper.toModel(studentRepository.save(student));
    }

    private CourseDTO createCourseObject() {
        Course course = new Course();
        course.setTitle("courseTitle"+ random.nextInt(1000));
        course.setDescription("testDescription");
        course.setTeacher(createTeacherObject());
        return courseMapper.toModel(courseRepository.save(course));
    }

    private Teacher createTeacherObject() {
        Teacher teacher = new Teacher();
        teacher.setName("teacherName");
        teacher.setPassword("testPass");
        teacher.setEmail("testEmail"+ random.nextInt(1000));
        teacher.setPhone("+20123548474561");
        return teacherRepository.save(teacher);
    }
}