package com.resume.resumeChecker.repository;

import com.resume.resumeChecker.model.JobDescription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JobDescriptionRepository extends JpaRepository<JobDescription, UUID> {
    List<JobDescription> findByActiveTrue();
}