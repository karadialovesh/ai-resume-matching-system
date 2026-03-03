package com.resume.resumeChecker.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "resumes")
@Data
public class Resume {

    @Id
    private UUID id;

    private String name;

    private String email;

    @Column(columnDefinition = "TEXT")
    private String content;


    @Column(columnDefinition = "vector(1024)")
    private float[] embedding;

    private String sourceType;

    private LocalDateTime createdAt;
}