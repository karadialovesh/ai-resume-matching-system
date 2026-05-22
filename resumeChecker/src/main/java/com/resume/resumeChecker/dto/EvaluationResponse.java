package com.resume.resumeChecker.dto;

import java.util.List;

public record EvaluationResponse(

        String rate,
        String decision,
        List<String> skillsMatched,
        String impact,
        List<String> gaps

) {}