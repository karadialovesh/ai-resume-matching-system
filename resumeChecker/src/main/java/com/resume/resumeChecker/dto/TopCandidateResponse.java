package com.resume.resumeChecker.dto;

public record TopCandidateResponse(

        String name,
        String email,
        String rate,
        String decision

) {}