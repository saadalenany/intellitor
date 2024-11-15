package com.intellitor.course.utils;

import com.intellitor.common.dtos.*;
import com.intellitor.common.entities.*;
import com.intellitor.common.mappers.*;
import com.intellitor.course.repos.CourseRepository;
import com.intellitor.course.repos.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class ObjectMaker {

    @Autowired
    public CourseRepository courseRepository;

    @Autowired
    public TeacherRepository teacherRepository;


    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private TeacherMapper teacherMapper;

    public final Long wrongId = 100L;
    public final String wrongValue = "wrong";
    private final Random random = new Random();

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

}
