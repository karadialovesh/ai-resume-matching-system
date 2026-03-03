package com.resume.resumeChecker.controller;

import com.resume.resumeChecker.dto.ApplyTextRequest;
import com.resume.resumeChecker.dto.EvaluationResponse;
import com.resume.resumeChecker.service.ResumeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/apply")
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping("/text")
    public EvaluationResponse applyText(@Valid @RequestBody ApplyTextRequest request) {
        return resumeService.processTextResume(request);
    }

    @PostMapping(value = "/pdf", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public EvaluationResponse applyPdf(
            @RequestParam @NotNull UUID jobId,
            @RequestParam String name,
            @RequestParam @Email String email,
            @RequestParam MultipartFile resume) {

        return resumeService.processPdfResume(jobId, name, email, resume);
    }
}