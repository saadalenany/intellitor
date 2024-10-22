package com.intellitor.common.repositories;

import com.intellitor.common.entities.QandA;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QandARepository extends JpaRepository<QandA, Long> {
}
