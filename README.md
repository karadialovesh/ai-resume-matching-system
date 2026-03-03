Ai Resume Matching System

This is a backend project I built to understand how AI can be used to automate resume screening.
The idea is simple:
Instead of manually checking every resume, the system compares resumes with a job description and gives a rating, matched skills, gaps, and a final decision (Shortlist / Reject).

I built this project to learn:
- Spring Boot backend development
- Working with PostgreSQL
- Integrating AI models for evaluation
- Basic caching with Redis

---

what this project does

1. HR creates a job description.
2. Candidates apply with either text or PDF resumes.
3. The system:
   - Extracts resume content
   - Generates embeddings
   - Stores them in PostgreSQL using pgvector
   - Uses AI to evaluate the resume
4. HR can retrieve top matching candidates for a job.

---

Tech Stack

- Java 17( better compatability)
- Spring Boot
- PostgreSQL
- pgvector
- Redis
- Ollama (for LLM & embeddings)

---
API endPoints

create jobs
POST /jobs

apply job text
POST /apply/text

apply job pdf
POST /apply/pdf

get top candidates
GET /hr/{jobId}/top-candidates

---
work this works-

Instead of evaluating all resumes with AI (which is slow and costly), I implemented a simple flow:

1. Generate embedding for job description
2. Find top similar resumes using pgvector
3. Send only top results to AI for evaluation
4. Return ranked candidates

Future improvements
- Hybrid keyword + vector search
- Better validation for PDF uploads
- Asynchronous AI processing
- Improved ranking logic
---

