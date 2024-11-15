package com.intellitor.enrollment.repos;

import com.intellitor.common.entities.Course;
import com.intellitor.common.entities.Enrollment;
import com.intellitor.common.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    Optional<Enrollment> findByStudentAndCourse(Student student, Course course);
}
