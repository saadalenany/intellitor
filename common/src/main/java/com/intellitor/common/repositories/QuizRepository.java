package com.intellitor.common.repositories;

import com.intellitor.common.entities.Enrollment;
import com.intellitor.common.entities.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    Optional<Quiz> findByEnrollment(Enrollment enrollment);
}
