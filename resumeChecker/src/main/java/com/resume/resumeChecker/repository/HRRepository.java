package com.resume.resumeChecker.repository;

import com.resume.resumeChecker.model.HR;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface HRRepository extends JpaRepository<HR, Long> {
    Optional<HR> findByUsername(String username);
}
