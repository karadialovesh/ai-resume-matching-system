package com.resume.resumeChecker.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record JobResponse(

        UUID id,
        String content,
        Boolean active,
        LocalDateTime createdAt

) {}