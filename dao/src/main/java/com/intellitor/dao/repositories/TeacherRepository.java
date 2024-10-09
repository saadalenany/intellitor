package com.intellitor.dao.repositories;

import com.intellitor.dao.entities.Teacher;
import com.intellitor.dao.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    Optional<Teacher> findByEmailAndPassword(String email, String password);
}
