package com.resume.resumeChecker.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.resume.resumeChecker.dto.EvaluationResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AIService {

    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;

    public AIService(ChatClient.Builder builder, ObjectMapper objectMapper) {
        this.chatClient = builder.build();
        this.objectMapper = objectMapper;
    }

    public EvaluationResponse evaluate(String job, String resume) {

        String prompt = """
            You are a strict resume evaluator.
            STRICT RULES:
                - Use ONLY resume content.
                - Do NOT fabricate skills.
                - Do NOT assume anything.
                - No explanation text.
                - No markdown.
                - Return ONLY valid raw JSON.
                - Output must start with { and end with }.
            Job Description:
            %s
            Resume:
            %s
            Impact Definition:
                - If resume contains measurable achievements, summarize them.
                - If no measurable impact is mentioned, summarize strongest professional strength from resume.
                - If nothing meaningful is found, return "No measurable impact mentioned".
            
            Return EXACT JSON format:
            {
              "rate": "8 out of 10",
              "skillsMatched": [],
              "impact": "Short summary based strictly on resume",
              "gaps": [],
              "decision": "Shortlist"
            }
            """.formatted(job, resume);

        String rawResponse = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

        String jsonOnly = extractJson(rawResponse);

        try {
            return objectMapper.readValue(jsonOnly, EvaluationResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Invalid AI JSON response");
        }
    }


    private String extractJson(String response) {
        int start = response.indexOf("{");
        int end = response.lastIndexOf("}");

        if (start == -1 || end == -1 || end <= start) {
            throw new RuntimeException("No valid JSON found in AI response");
        }

        return response.substring(start, end + 1);
    }
}