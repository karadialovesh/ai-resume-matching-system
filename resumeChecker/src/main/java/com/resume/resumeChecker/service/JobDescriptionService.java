package com.resume.resumeChecker.service;

import com.resume.resumeChecker.dto.JobDescriptionRequest;
import com.resume.resumeChecker.model.JobDescription;
import com.resume.resumeChecker.repository.JobDescriptionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class JobDescriptionService {

    private final JobDescriptionRepository repository;

    public JobDescriptionService(JobDescriptionRepository repository) {
        this.repository = repository;
    }

    public UUID create(JobDescriptionRequest request) {

        JobDescription job = new JobDescription();
        job.setId(UUID.randomUUID());
        job.setContent(request.content());
        job.setActive(true);
        job.setCreatedAt(LocalDateTime.now());

        repository.save(job);

        return job.getId();
    }

    public JobDescription getJob(UUID jobId) {
        return repository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
    }

    public List<JobDescription> getActiveJobs() {
        return repository.findByActiveTrue();
    }

    public void closeJob(UUID jobId) {
        JobDescription job = getJob(jobId);
        job.setActive(false);
        repository.save(job);
    }
}