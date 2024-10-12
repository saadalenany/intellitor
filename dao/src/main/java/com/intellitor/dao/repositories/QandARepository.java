package com.intellitor.dao.repositories;

import com.intellitor.dao.entities.QandA;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QandARepository extends JpaRepository<QandA, Long> {
}
