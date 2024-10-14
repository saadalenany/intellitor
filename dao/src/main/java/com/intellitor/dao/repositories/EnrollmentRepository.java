package com.intellitor.dao.repositories;

import com.intellitor.dao.entities.Course;
import com.intellitor.dao.entities.Enrollment;
import com.intellitor.dao.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    Optional<Enrollment> findByStudentAndCourse(Student student, Course course);
}
