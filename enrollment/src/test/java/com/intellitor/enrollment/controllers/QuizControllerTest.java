package com.intellitor.enrollment.controllers;

import com.intellitor.common.dtos.EnrollmentDTO;
import com.intellitor.common.dtos.QuizDTO;
import com.intellitor.common.utils.ErrorMessages;
import com.intellitor.common.utils.ObjectNames;
import com.intellitor.common.utils.Response;
import com.intellitor.common.entities.*;
import com.intellitor.common.utils.BaseTest;
import com.intellitor.enrollment.utils.ObjectMaker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class QuizControllerTest extends BaseTest {

    private final String path = "/quiz";

    @Autowired
    private ObjectMaker objectMaker;

    @BeforeEach
    public void setUp() {
        objectMaker.enrollmentRepository.deleteAll();
        objectMaker.studentRepository.deleteAll();
        objectMaker.courseRepository.deleteAll();
        objectMaker.teacherRepository.deleteAll();
        objectMaker.quizRepository.deleteAll();
    }

    @Test
    void testCreateQuiz_success() throws Exception {
        QuizDTO quizObject = objectMaker.createQuizDTO();
        Response<QuizDTO> response = postForObject(path, quizObject, Response.class, QuizDTO.class);
        assertEquals(200, response.getStatus());
        QuizDTO postedObject = response.getContent();

        final Long quizId = postedObject.getId();
        runInTransaction(status -> {
            Quiz expectedQuiz = objectMaker.quizRepository.findById(quizId).orElseGet(() -> fail("Expected QuizEntity not found"));

            assertEquals(quizId, expectedQuiz.getId(), "Quiz wasn't created successfully");
            return null;
        });
    }

    @Test
    void testCreateQuiz_failureNoEnrollment() throws Exception {
        QuizDTO quizObject = objectMaker.createQuizDTO();
        quizObject.setEnrollmentId(objectMaker.wrongId);
        Response<String> errResponse = postForError(path, quizObject);
        assertEquals(400, errResponse.getStatus());
        assertEquals(String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.ENROLLMENT, objectMaker.wrongId), errResponse.getContent());
    }

    @Test
    void testCreateQuiz_failureDuplicateEnrollment() throws Exception {
        QuizDTO quizObject = objectMaker.createQuizDTO();
        Response<QuizDTO> response = postForObject(path, quizObject, Response.class, QuizDTO.class);
        assertEquals(200, response.getStatus());

        Response<String> errResponse = postForError(path, quizObject);
        assertEquals(400, errResponse.getStatus());
        assertEquals(String.format(ErrorMessages.OBJECT_FIELD_ALREADY_EXISTS, ObjectNames.QUIZ, "enrollment", quizObject.getEnrollmentId()), errResponse.getContent());
    }

    @Test
    void testGetQuizById_success() throws Exception {
        QuizDTO quizObject = objectMaker.createAndSaveQuizDTO();

        Response<QuizDTO> response = getForObject(String.format("%s/%d", path, quizObject.getId()), Response.class, QuizDTO.class);
        assertEquals(200, response.getStatus());
        QuizDTO gotObject = response.getContent();
        assertEquals(quizObject.getId(), gotObject.getId(), "Quiz wasn't found");
    }

    @Test
    void testGetQuizById_failure() throws Exception {
        Response<String> response = getForError(String.format("%s/%s", path, objectMaker.wrongId));
        assertEquals(400, response.getStatus());
        assertEquals(String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.QUIZ, objectMaker.wrongId), response.getContent());
    }

    @Test
    void testUpdateQuiz_success() throws Exception {
        QuizDTO quizObject = objectMaker.createAndSaveQuizDTO();

        EnrollmentDTO newEnrollment = objectMaker.createAndSaveEnrollmentDTO();
        quizObject.setEnrollmentId(newEnrollment.getId());
        Response<QuizDTO> response = putForObject(path, quizObject, Response.class, QuizDTO.class);
        runInTransaction(status -> {
            Quiz expectedQuiz = objectMaker.quizRepository.findById(response.getContent().getId()).orElseGet(() -> fail("Expected QuizEntity not found"));

            assertEquals(newEnrollment.getId(), expectedQuiz.getEnrollment().getId(), "Quiz description wasn't updated successfully");
            return null;
        });
    }

    @Test
    void testUpdateQuiz_failure() throws Exception {
        QuizDTO quizObject = objectMaker.createQuizDTO();
        quizObject.setId(objectMaker.wrongId);
        Response<String> errResponse = putForError(path, quizObject);
        assertEquals(400, errResponse.getStatus());
        assertEquals(String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.QUIZ, quizObject.getId()), errResponse.getContent());
    }

    @Test
    void testDeleteQuiz_success() throws Exception {
        QuizDTO quizObject = objectMaker.createAndSaveQuizDTO();

        Response<QuizDTO> response = deleteForObject(path, String.valueOf(quizObject.getId()), Response.class, QuizDTO.class);
        assertEquals(200, response.getStatus(), "Quiz wasn't deleted successfully");
        assertEquals(quizObject.getId(), response.getContent().getId(), "Quiz wasn't deleted successfully");
    }

    @Test
    void testDeleteQuiz_failure() throws Exception {
        Response<String> response = deleteForError(path, String.valueOf(objectMaker.wrongId));
        assertEquals(400, response.getStatus(), "Quiz was found already");
        assertEquals(String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.QUIZ, objectMaker.wrongId), response.getContent());
    }
}