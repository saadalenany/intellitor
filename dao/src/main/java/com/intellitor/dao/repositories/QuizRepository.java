package com.intellitor.dao.repositories;

import com.intellitor.dao.entities.Enrollment;
import com.intellitor.dao.entities.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    Optional<Quiz> findByEnrollment(Enrollment enrollment);
}
