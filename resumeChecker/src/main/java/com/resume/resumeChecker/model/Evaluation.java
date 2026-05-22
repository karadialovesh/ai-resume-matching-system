package com.resume.resumeChecker.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "evaluations",
        indexes = {
                @Index(name = "idx_job_id", columnList = "job_id"),
                @Index(name = "idx_resume_id", columnList = "resume_id")
        })
@Data
public class Evaluation {

    @Id
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "resume_id")
    private Resume resume;

    @ManyToOne(optional = false)
    @JoinColumn(name = "job_id")
    private JobDescription job;

    private String rate;

    private String decision;

    @Column(columnDefinition = "TEXT")
    private String impact;

    @Column(columnDefinition = "TEXT[]")
    private String[] skillsMatched;

    @Column(columnDefinition = "TEXT[]")
    private String[] gaps;
    private LocalDateTime createdAt;
}