package com.resume.resumeChecker.service;

import com.resume.resumeChecker.dto.JobDescriptionRequest;
import com.resume.resumeChecker.model.HR;
import com.resume.resumeChecker.model.JobDescription;
import com.resume.resumeChecker.repository.HRRepository;
import com.resume.resumeChecker.repository.JobDescriptionRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class JobDescriptionService {

    private final JobDescriptionRepository repository;
    private final HRRepository hrRepository;

    public JobDescriptionService(
            JobDescriptionRepository repository,
            HRRepository hrRepository
    ) {
        this.repository = repository;
        this.hrRepository = hrRepository;
    }

    public UUID create(JobDescriptionRequest request) {

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        String username = auth.getName();

        HR hr = hrRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("HR not found"));

        JobDescription job = new JobDescription();

        job.setId(UUID.randomUUID());
        job.setContent(request.content());
        job.setActive(true);
        job.setCreatedAt(LocalDateTime.now());

        job.setCreatedById(hr.getId());
        job.setCreatedByName(hr.getName());

        repository.save(job);

        return job.getId();
    }

    public JobDescription getJob(UUID jobId) {
        return repository.findById(jobId)
                .orElseThrow(() ->
                        new RuntimeException("Job not found"));
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