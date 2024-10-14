package com.intellitor.dao.controllers;

import com.intellitor.common.dtos.CourseDTO;
import com.intellitor.common.dtos.EnrollmentDTO;
import com.intellitor.common.dtos.QuizDTO;
import com.intellitor.common.dtos.StudentDTO;
import com.intellitor.common.utils.ErrorMessages;
import com.intellitor.common.utils.ObjectNames;
import com.intellitor.common.utils.Response;
import com.intellitor.dao.entities.*;
import com.intellitor.dao.mappers.CourseMapper;
import com.intellitor.dao.mappers.EnrollmentMapper;
import com.intellitor.dao.mappers.QuizMapper;
import com.intellitor.dao.mappers.StudentMapper;
import com.intellitor.dao.repositories.*;
import com.intellitor.dao.utils.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class QuizControllerTest extends BaseTest {

    private final String path = "/quiz";
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
    private EnrollmentMapper enrollmentMapper;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuizMapper quizMapper;

    @BeforeEach
    public void setUp() {
        // Clear the database before each test
        enrollmentRepository.deleteAll();
        studentRepository.deleteAll();
        courseRepository.deleteAll();
        teacherRepository.deleteAll();
        quizRepository.deleteAll();
    }

    @Test
    void testCreateQuiz_success() throws Exception {
        QuizDTO quizObject = createQuizObject();
        Response<QuizDTO> response = postForObject(path, quizObject, Response.class, QuizDTO.class);
        assertEquals(200, response.getStatus());
        QuizDTO postedObject = response.getContent();

        final Long quizId = postedObject.getId();
        runInTransaction(status -> {
            Quiz expectedQuiz = quizRepository.findById(quizId).orElseGet(() -> fail("Expected QuizEntity not found"));

            assertEquals(quizId, expectedQuiz.getId(), "Quiz wasn't created successfully");
            return null;
        });
    }

    @Test
    void testCreateQuiz_failureNoEnrollment() throws Exception {
        QuizDTO quizObject = createQuizObject();
        final Long WRONG_ID = 100L;
        quizObject.setEnrollmentId(WRONG_ID);
        Response<String> errResponse = postForObject(path, quizObject, Response.class, String.class);
        assertEquals(400, errResponse.getStatus());
        assertEquals(String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.ENROLLMENT, WRONG_ID), errResponse.getContent());
    }

    @Test
    void testCreateQuiz_failureDuplicateEnrollment() throws Exception {
        QuizDTO quizObject = createQuizObject();
        Response<QuizDTO> response = postForObject(path, quizObject, Response.class, QuizDTO.class);
        assertEquals(200, response.getStatus());

        Response<String> errResponse = postForObject(path, quizObject, Response.class, String.class);
        assertEquals(400, errResponse.getStatus());
        assertEquals(String.format(ErrorMessages.OBJECT_FIELD_ALREADY_EXISTS, ObjectNames.QUIZ, "enrollment", quizObject.getEnrollmentId()), errResponse.getContent());
    }

    @Test
    void testGetQuizById_success() throws Exception {
        QuizDTO quizObject = createQuizObject();
        Quiz entity = quizMapper.toEntity(quizObject);
        Quiz saved = quizRepository.save(entity);

        Response<QuizDTO> response = getForObject(String.format("%s/%d", path, saved.getId()), Response.class, QuizDTO.class);
        assertEquals(200, response.getStatus());
        QuizDTO gotObject = response.getContent();
        assertEquals(saved.getId(), gotObject.getId(), "Quiz wasn't found");
    }

    @Test
    void testGetQuizById_failure() throws Exception {
        final String WRONG_ID = "100";
        Response<String> response = getForObject(String.format("%s/%s", path, WRONG_ID), Response.class, String.class);
        assertEquals(400, response.getStatus());
        assertEquals(String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.QUIZ, WRONG_ID), response.getContent());
    }

    @Test
    void testUpdateQuiz_success() throws Exception {
        QuizDTO quizObject = createQuizObject();
        Quiz entity = quizMapper.toEntity(quizObject);
        Quiz saved = quizRepository.save(entity);

        Enrollment newEnrollment = createEnrollmentObject();
        saved.setEnrollment(newEnrollment);
        QuizDTO model = quizMapper.toModel(saved);
        Response<QuizDTO> response = putForObject(path, model, Response.class, QuizDTO.class);
        runInTransaction(status -> {
            Quiz expectedQuiz = quizRepository.findById(response.getContent().getId()).orElseGet(() -> fail("Expected QuizEntity not found"));

            assertEquals(newEnrollment.getId(), expectedQuiz.getEnrollment().getId(), "Quiz description wasn't updated successfully");
            return null;
        });
    }

    @Test
    void testUpdateQuiz_failure() throws Exception {
        QuizDTO quizObject = createQuizObject();
        quizObject.setId(100L);
        Response<String> errResponse = putForObject(path, quizObject, Response.class, String.class);
        assertEquals(400, errResponse.getStatus());
        assertEquals(String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.QUIZ, quizObject.getId()), errResponse.getContent());
    }

    @Test
    void testDeleteQuiz_success() throws Exception {
        QuizDTO quizObject = createQuizObject();
        Quiz entity = quizMapper.toEntity(quizObject);
        Quiz saved = quizRepository.save(entity);

        Response<QuizDTO> response = deleteForObject(path, String.valueOf(saved.getId()), Response.class, QuizDTO.class);
        assertEquals(200, response.getStatus(), "Quiz wasn't deleted successfully");
        assertEquals(saved.getId(), response.getContent().getId(), "Quiz wasn't deleted successfully");
    }

    @Test
    void testDeleteQuiz_failure() throws Exception {
        final Long WRONG_ID = 100L;
        Response<String> response = deleteForObject(path, String.valueOf(WRONG_ID), Response.class, String.class);
        assertEquals(400, response.getStatus(), "Quiz was found already");
        assertEquals(String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.QUIZ, WRONG_ID), response.getContent());
    }

    private QuizDTO createQuizObject() {
        QuizDTO quiz = new QuizDTO();
        quiz.setEnrollmentId(createEnrollmentObject().getId());
        return quiz;
    }

    private Enrollment createEnrollmentObject() {
        Enrollment enrollment = new Enrollment();
        enrollment.setCourse(createCourseObject());
        enrollment.setStudent(createStudentObject());
        return enrollmentRepository.save(enrollment);
    }

    private Student createStudentObject() {
        Student student = new Student();
        student.setName("studentName");
        student.setPassword("testPass");
        student.setEmail("testEmail" + random.nextInt(1000));
        student.setPhone("+20123548474561");
        return studentRepository.save(student);
    }

    private Course createCourseObject() {
        Course course = new Course();
        course.setTitle("courseTitle"+ random.nextInt(1000));
        course.setDescription("testDescription");
        course.setTeacher(createTeacherObject());
        return courseRepository.save(course);
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