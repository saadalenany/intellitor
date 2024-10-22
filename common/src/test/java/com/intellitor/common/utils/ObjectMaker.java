package com.intellitor.common.utils;

import com.intellitor.common.dtos.*;
import com.intellitor.common.entities.*;
import com.intellitor.common.mappers.*;
import com.intellitor.common.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class ObjectMaker {

    @Autowired
    public TeacherRepository teacherRepository;

    @Autowired
    public CourseRepository courseRepository;

    @Autowired
    public StudentRepository studentRepository;

    @Autowired
    public EnrollmentRepository enrollmentRepository;

    @Autowired
    public QuizRepository quizRepository;

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private EnrollmentMapper enrollmentMapper;

    @Autowired
    private QuizMapper quizMapper;

    public final Long wrongId = 100L;
    public final String wrongValue = "wrong";
    private final Random random = new Random();

    /************************************************************************
     * Teacher methods
     ************************************************************************/

    public TeacherDTO createTeacherDTO() {
        TeacherDTO teacher = new TeacherDTO();
        teacher.setName("teacherName");
        teacher.setPassword("testPass");
        teacher.setEmail("testEmail"+random.nextInt(1000));
        teacher.setPhone("+20123548474561");
        return teacher;
    }

    private Teacher createTeacherEntity() {
        TeacherDTO teacherDTO = createTeacherDTO();
        Teacher teacher = teacherMapper.toEntity(teacherDTO);
        return teacherRepository.save(teacher);
    }

    public TeacherDTO createAndSaveTeacherDTO() {
        return teacherMapper.toModel(createTeacherEntity());
    }

    /************************************************************************
    * Student methods
    ************************************************************************/

    public StudentDTO createStudentDTO() {
        StudentDTO student = new StudentDTO();
        student.setName("studentName");
        student.setPassword("testPass");
        student.setEmail("testEmail"+random.nextInt(1000));
        student.setPhone("+20123548474561");
        return student;
    }

    private Student createStudentEntity() {
        StudentDTO studentDTO = createStudentDTO();
        Student student = studentMapper.toEntity(studentDTO);
        return studentRepository.save(student);
    }

    public StudentDTO createAndSaveStudentDTO() {
        return studentMapper.toModel(createStudentEntity());
    }

    /************************************************************************
     * Course methods
     ************************************************************************/

    public CourseDTO createCourseDTO() {
        CourseDTO course = new CourseDTO();
        course.setTitle("courseTitle"+random.nextInt(1000));
        course.setDescription("testDescription");
        course.setTeacherId(createTeacherEntity().getId());
        return course;
    }

    private Course createCourseEntity() {
        CourseDTO courseDTO = createCourseDTO();
        Course course = courseMapper.toEntity(courseDTO);
        return courseRepository.save(course);
    }

    public CourseDTO createAndSaveCourseDTO() {
        return courseMapper.toModel(createCourseEntity());
    }

    /************************************************************************
     * Enrollment methods
     ************************************************************************/

    public EnrollmentDTO createEnrollmentDTO() {
        EnrollmentDTO enrollment = new EnrollmentDTO();
        enrollment.setCourseId(createAndSaveCourseDTO().getId());
        enrollment.setStudentId(createAndSaveStudentDTO().getId());
        return enrollment;
    }

    private Enrollment createEnrollmentEntity() {
        EnrollmentDTO enrollmentDTO = createEnrollmentDTO();
        Enrollment enrollment = enrollmentMapper.toEntity(enrollmentDTO);
        return enrollmentRepository.save(enrollment);
    }

    public EnrollmentDTO createAndSaveEnrollmentDTO() {
        return enrollmentMapper.toModel(createEnrollmentEntity());
    }

    /************************************************************************
     * Quiz methods
     ************************************************************************/

    public QuizDTO createQuizDTO() {
        QuizDTO quiz = new QuizDTO();
        quiz.setEnrollmentId(createAndSaveEnrollmentDTO().getId());
        return quiz;
    }

    private Quiz createQuizEntity() {
        QuizDTO quizDTO = createQuizDTO();
        Quiz quiz = quizMapper.toEntity(quizDTO);
        return quizRepository.save(quiz);
    }

    public QuizDTO createAndSaveQuizDTO() {
        return quizMapper.toModel(createQuizEntity());
    }

}
