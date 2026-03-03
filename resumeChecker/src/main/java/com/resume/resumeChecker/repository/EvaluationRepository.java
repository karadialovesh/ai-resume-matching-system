package com.resume.resumeChecker.repository;

import com.resume.resumeChecker.model.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EvaluationRepository extends JpaRepository<Evaluation, UUID> {


    List<Evaluation> findByJob_Id(UUID jobId);

    List<Evaluation> findByJob_IdAndDecisionIgnoreCase(UUID jobId, String decision);

    boolean existsByJob_IdAndResume_EmailIgnoreCase(UUID jobId, String email);
}