package com.intellitor.enrollment.controllers;

import com.intellitor.common.dtos.EnrollmentDTO;
import com.intellitor.common.dtos.StudentDTO;
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

class EnrollmentControllerTest extends BaseTest {

    private final String path = "/enrollment";

    @Autowired
    private ObjectMaker objectMaker;

    @BeforeEach
    public void setUp() {
        objectMaker.enrollmentRepository.deleteAll();
        objectMaker.studentRepository.deleteAll();
        objectMaker.courseRepository.deleteAll();
        objectMaker.teacherRepository.deleteAll();
    }

    @Test
    void testCreateEnrollment_success() throws Exception {
        EnrollmentDTO enrollmentObject = objectMaker.createEnrollmentDTO();
        Response<EnrollmentDTO> response = postForObject(path, enrollmentObject, Response.class, EnrollmentDTO.class);
        assertEquals(200, response.getStatus());
        EnrollmentDTO postedObject = response.getContent();

        final Long enrollmentId = postedObject.getId();
        runInTransaction(status -> {
            Enrollment expectedEnrollment = objectMaker.enrollmentRepository.findById(enrollmentId).orElseGet(() -> fail("Expected EnrollmentEntity not found"));

            assertEquals(enrollmentId, expectedEnrollment.getId(), "Enrollment wasn't created successfully");
            return null;
        });
    }

    @Test
    void testCreateEnrollment_failureWrongStudent() throws Exception {
        EnrollmentDTO enrollmentObject = new EnrollmentDTO();

        enrollmentObject.setStudentId(objectMaker.wrongId);
        Response<String> response = postForError(path, enrollmentObject);
        assertEquals(400, response.getStatus());
        assertEquals(String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.STUDENT, objectMaker.wrongId), response.getContent());
    }

    @Test
    void testCreateEnrollment_failureWrongCourse() throws Exception {
        EnrollmentDTO enrollmentObject = new EnrollmentDTO();

        enrollmentObject.setStudentId(objectMaker.createAndSaveStudentDTO().getId());
        enrollmentObject.setCourseId(objectMaker.wrongId);
        Response<String> response = postForError(path, enrollmentObject);
        assertEquals(400, response.getStatus());
        assertEquals(String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.COURSE, objectMaker.wrongId), response.getContent());
    }

    @Test
    void testCreateEnrollment_failureDuplicateStudentAndCourse() throws Exception {
        EnrollmentDTO enrollmentObject = objectMaker.createEnrollmentDTO();
        Response<EnrollmentDTO> successResponse = postForObject(path, enrollmentObject, Response.class, EnrollmentDTO.class);
        assertEquals(200, successResponse.getStatus());

        Response<String> response = postForError(path, enrollmentObject);
        assertEquals(400, response.getStatus());

        runInTransaction(status -> {
            Course course = objectMaker.courseRepository.findById(successResponse.getContent().getCourseId()).orElse(null);
            Student student = objectMaker.studentRepository.findById(successResponse.getContent().getStudentId()).orElse(null);
            assertEquals(
                    String.format(ErrorMessages.OBJECT_FIELD_ALREADY_EXISTS, ObjectNames.ENROLLMENT, "{student, course}", student.getEmail()+" , "+course.getTitle()),
                    response.getContent()
            );
            return null;
        });
    }

    @Test
    void testGetEnrollmentById_success() throws Exception {
        EnrollmentDTO enrollmentObject = objectMaker.createAndSaveEnrollmentDTO();

        Response<EnrollmentDTO> response = getForObject(String.format("%s/%d", path, enrollmentObject.getId()), Response.class, EnrollmentDTO.class);
        assertEquals(200, response.getStatus());
        EnrollmentDTO gotObject = response.getContent();
        assertEquals(enrollmentObject.getId(), gotObject.getId(), "Enrollment wasn't found");
    }

    @Test
    void testGetEnrollmentById_failure() throws Exception {
        Response<String> response = getForError(String.format("%s/%s", path, objectMaker.wrongId));
        assertEquals(400, response.getStatus());
        assertEquals(String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.ENROLLMENT, objectMaker.wrongId), response.getContent());
    }

    @Test
    void testUpdateEnrollment_success() throws Exception {
        EnrollmentDTO enrollmentObject = objectMaker.createAndSaveEnrollmentDTO();

        StudentDTO newStudent = objectMaker.createAndSaveStudentDTO();
        enrollmentObject.setStudentId(newStudent.getId());
        Response<EnrollmentDTO> response = putForObject(path, enrollmentObject, Response.class, EnrollmentDTO.class);
        runInTransaction(status -> {
            Enrollment expectedEnrollment = objectMaker.enrollmentRepository.findById(response.getContent().getId()).orElseGet(() -> fail("Expected EnrollmentEntity not found"));

            assertEquals(newStudent.getEmail(), expectedEnrollment.getStudent().getEmail(), "Enrollment description wasn't updated successfully");
            return null;
        });
    }

    @Test
    void testUpdateEnrollment_failure() throws Exception {
        EnrollmentDTO enrollmentObject = objectMaker.createEnrollmentDTO();
        enrollmentObject.setId(objectMaker.wrongId);
        Response<String> errResponse = putForError(path, enrollmentObject);
        assertEquals(400, errResponse.getStatus());
        assertEquals(String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.ENROLLMENT, enrollmentObject.getId()), errResponse.getContent());
    }

    @Test
    void testDeleteEnrollment_success() throws Exception {
        EnrollmentDTO enrollmentObject = objectMaker.createAndSaveEnrollmentDTO();

        Response<EnrollmentDTO> response = deleteForObject(path, String.valueOf(enrollmentObject.getId()), Response.class, EnrollmentDTO.class);
        assertEquals(200, response.getStatus(), "Enrollment wasn't deleted successfully");
        assertEquals(enrollmentObject.getId(), response.getContent().getId(), "Enrollment wasn't deleted successfully");
    }

    @Test
    void testDeleteEnrollment_failure() throws Exception {
        Response<String> response = deleteForError(path, String.valueOf(objectMaker.wrongId));
        assertEquals(400, response.getStatus(), "Enrollment was found already");
        assertEquals(String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.ENROLLMENT, objectMaker.wrongId), response.getContent());
    }
}