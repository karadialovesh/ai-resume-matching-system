package com.resume.resumeChecker.service;

import com.resume.resumeChecker.dto.TopCandidateCache;
import com.resume.resumeChecker.dto.EvaluationResponse;
import com.resume.resumeChecker.dto.TopCandidateResponse;
import com.resume.resumeChecker.model.JobDescription;
import com.resume.resumeChecker.repository.JobDescriptionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class HRService {

        private final JobDescriptionRepository jobRepository;
        private final EmbeddingService embeddingService;
        private final AIService aiService;

        @PersistenceContext
        private EntityManager entityManager;

        public HRService(JobDescriptionRepository jobRepository,
                        EmbeddingService embeddingService,
                        AIService aiService) {
                this.jobRepository = jobRepository;
                this.embeddingService = embeddingService;
                this.aiService = aiService;
        }

        @Cacheable(value = "topCandidates", key = "#jobId.toString() + '_' + #limit")
        public TopCandidateCache getTopCandidates(UUID jobId, int limit) {

                JobDescription job = jobRepository.findById(jobId)
                                .orElseThrow(() -> new RuntimeException("Job not found"));

                String jobContent = job.getContent();

                float[] jobEmbeddingArray = embeddingService.generateEmbedding(jobContent);

                StringBuilder sb = new StringBuilder();
                sb.append("[");

                for (int i = 0; i < jobEmbeddingArray.length; i++) {
                        sb.append(String.format("%.8f", jobEmbeddingArray[i]));
                        if (i < jobEmbeddingArray.length - 1) {
                                sb.append(",");
                        }
                }

                sb.append("]");
                String jobEmbedding = sb.toString();

                String sql = """
                                    SELECT r.id, r.name, r.email, r.content
                                    FROM resumes r
                                    INNER JOIN evaluations e ON e.resume_id = r.id
                                    WHERE r.embedding IS NOT NULL
                                      AND e.job_id = CAST(:jobId AS uuid)
                                    ORDER BY r.embedding <-> CAST(:embedding AS vector)
                                    LIMIT :limit
                                """;

                List<Object[]> results = entityManager
                                .createNativeQuery(sql)
                                .setParameter("embedding", jobEmbedding)
                                .setParameter("jobId", jobId.toString())
                                .setParameter("limit", limit)
                                .getResultList();

                List<TopCandidateResponse> candidates = results.stream()
                                .map(row -> {

                                        String name = (String) row[1];
                                        String email = (String) row[2];
                                        String resumeContent = (String) row[3];

                                        EvaluationResponse aiResult = aiService.evaluate(jobContent, resumeContent);

                                        return new TopCandidateResponse(
                                                        name,
                                                        email,
                                                        aiResult.rate(),
                                                        aiResult.decision());
                                })
                                .toList();

                return new TopCandidateCache(candidates);
        }
}