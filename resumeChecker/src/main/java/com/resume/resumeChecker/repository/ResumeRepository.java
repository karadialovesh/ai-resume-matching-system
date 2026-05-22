package com.resume.resumeChecker.repository;

import com.resume.resumeChecker.model.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ResumeRepository extends JpaRepository<Resume, UUID> {
    @Query(value = """
    SELECT * FROM resumes
    WHERE embedding IS NOT NULL
    ORDER BY embedding <-> CAST(:embedding AS vector)
    LIMIT :limit
""", nativeQuery = true)
    List<Resume> findTopSimilar(
            @Param("embedding") String embedding,
            @Param("limit") int limit
    );
}