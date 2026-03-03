package com.resume.resumeChecker.controller;

import com.resume.resumeChecker.dto.JobDescriptionRequest;
import com.resume.resumeChecker.dto.JobResponse;
import com.resume.resumeChecker.model.JobDescription;
import com.resume.resumeChecker.service.JobDescriptionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/jobs")
public class JobDescriptionController {

    private final JobDescriptionService service;

    public JobDescriptionController(JobDescriptionService service) {
        this.service = service;
    }

    @PostMapping
    public JobResponse create(@Valid @RequestBody JobDescriptionRequest request) {

        UUID jobId = service.create(request);
        JobDescription job = service.getJob(jobId);

        return new JobResponse(
                job.getId(),
                job.getContent(),
                job.getActive(),
                job.getCreatedAt()
        );
    }

    @GetMapping("/active")
    public List<JobDescription> getActiveJobs() {
        return service.getActiveJobs();
    }

    @PatchMapping("/{jobId}/close")
    public void closeJob(@PathVariable UUID jobId) {
        service.closeJob(jobId);
    }
}