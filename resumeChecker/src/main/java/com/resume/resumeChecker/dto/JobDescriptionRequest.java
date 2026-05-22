package com.resume.resumeChecker.dto;

import jakarta.validation.constraints.NotBlank;

public record JobDescriptionRequest(

        @NotBlank(message = "Job description content cannot be empty")
        String content

) {}