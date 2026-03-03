package com.resume.resumeChecker.service;

import com.resume.resumeChecker.dto.ApplyTextRequest;
import com.resume.resumeChecker.dto.EvaluationResponse;
import com.resume.resumeChecker.model.Evaluation;
import com.resume.resumeChecker.model.JobDescription;
import com.resume.resumeChecker.model.Resume;
import com.resume.resumeChecker.repository.EvaluationRepository;
import com.resume.resumeChecker.repository.JobDescriptionRepository;
import com.resume.resumeChecker.repository.ResumeRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.cache.annotation.CacheEvict;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final EvaluationRepository evaluationRepository;
    private final JobDescriptionRepository jobRepository;
    private final AIService aiService;
    private final PdfService pdfService;
    private final EmbeddingService embeddingService;

    public ResumeService(ResumeRepository resumeRepository,
                         EvaluationRepository evaluationRepository,
                         JobDescriptionRepository jobRepository,
                         AIService aiService,
                         PdfService pdfService, EmbeddingService embeddingService) {
        this.resumeRepository = resumeRepository;
        this.evaluationRepository = evaluationRepository;
        this.jobRepository = jobRepository;
        this.aiService = aiService;
        this.pdfService = pdfService;
        this.embeddingService = embeddingService;
    }

    public EvaluationResponse processTextResume(ApplyTextRequest request) {

        JobDescription job = jobRepository.findById(request.jobId())
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!job.getActive()) {
            throw new RuntimeException("This job is closed");
        }

        if (evaluationRepository.existsByJob_IdAndResume_EmailIgnoreCase(
                request.jobId(), request.email())) {
            throw new RuntimeException("You already applied to this job");
        }

        return evaluateAndStore(job, request.name(), request.email(), request.content(), "TEXT");
    }

    public EvaluationResponse processPdfResume(UUID jobId,
                                               String name,
                                               String email,
                                               MultipartFile file) {

        JobDescription job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!job.getActive()) {
            throw new RuntimeException("This job is closed");
        }

        if (evaluationRepository.existsByJob_IdAndResume_EmailIgnoreCase(
                jobId, email)) {
            throw new RuntimeException("You already applied to this job");
        }

        String content = pdfService.extractText(file);


        return evaluateAndStore(job, name, email, content, "PDF");
    }
    @CacheEvict(value = "topCandidates", allEntries = true)
    private EvaluationResponse evaluateAndStore(JobDescription job,
                                                String name,
                                                String email,
                                                String content,
                                                String type) {

        Resume resume = new Resume();
        resume.setId(UUID.randomUUID());
        resume.setName(name);
        resume.setEmail(email);
        resume.setContent(content);
        resume.setSourceType(type);
        resume.setCreatedAt(LocalDateTime.now());
        float[] embedding = embeddingService.generateEmbedding(content);
        System.out.println("------------------------------------------------------");
        System.out.println(Arrays.toString(embedding));
        resume.setEmbedding(embedding);

        resumeRepository.save(resume);

        EvaluationResponse response = aiService.evaluate(job.getContent(), content);

        Evaluation evaluation = new Evaluation();
        evaluation.setId(UUID.randomUUID());
        evaluation.setResume(resume);
        evaluation.setJob(job);
        evaluation.setRate(response.rate());
        evaluation.setDecision(response.decision());
        evaluation.setImpact(response.impact());
        evaluation.setSkillsMatched(response.skillsMatched().toArray(new String[0]));
        evaluation.setGaps(response.gaps().toArray(new String[0]));
        evaluation.setCreatedAt(LocalDateTime.now());

        evaluationRepository.save(evaluation);

        return response;
    }
}