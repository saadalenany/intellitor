package com.intellitor.user.utils;

import com.intellitor.common.dtos.*;
import com.intellitor.common.entities.*;
import com.intellitor.common.mappers.*;
import com.intellitor.user.repos.StudentRepository;
import com.intellitor.user.repos.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class ObjectMaker {

    @Autowired
    public TeacherRepository teacherRepository;

    @Autowired
    public StudentRepository studentRepository;

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private StudentMapper studentMapper;

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
}
