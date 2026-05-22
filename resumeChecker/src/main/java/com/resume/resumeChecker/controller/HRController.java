package com.resume.resumeChecker.controller;

import com.resume.resumeChecker.dto.TopCandidateResponse;
import com.resume.resumeChecker.service.HRService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/hr")
public class HRController {

    private final HRService hrService;

    public HRController(HRService hrService) {
        this.hrService = hrService;
    }

    @GetMapping("/{jobId}/top-candidates")
    public List<TopCandidateResponse> getTopCandidates(
            @PathVariable UUID jobId,
            @RequestParam(defaultValue = "5") int limit) {

        return hrService.getTopCandidates(jobId, limit).getCandidates();
    }

}