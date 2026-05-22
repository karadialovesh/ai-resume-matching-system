package com.resume.resumeChecker.dto;

import com.resume.resumeChecker.dto.TopCandidateResponse;
import lombok.Data;

import java.util.List;

@Data
public class TopCandidateCache {

    private List<TopCandidateResponse> candidates;

    public TopCandidateCache() {}

    public TopCandidateCache(List<TopCandidateResponse> candidates) {
        this.candidates = candidates;
    }
}