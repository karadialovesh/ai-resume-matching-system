# AI Resume Matching System

This is a backend application built with Spring Boot that helps filter resumes. It takes a job description and a candidate's resume, and uses a local AI model to figure out if the candidate is a good match for the job.

## Tech Stack
- Java 17 and Spring Boot 3.2.5
- PostgreSQL (for storing data)
- Redis (for caching results)
- Ollama (for running AI models locally)
- Apache PDFBox (for reading PDF files)

## Requirements

You'll need a few things running locally before you start the application:

1. A PostgreSQL database running on port 5432. The database should be named `resume_ai` and the credentials should be `postgres` / `postgres`. You must create the vector extension for vector search by running `CREATE EXTENSION IF NOT EXISTS vector;` in your database.
2. A Redis server running on port 6379.
3. Ollama installed and running on your machine.

Open your terminal and run these commands:
```bash
ollama pull llama3
ollama pull nomic-embed-text
```

## How to Run

1. Firstly run PostgreSQL, Redis, and Ollama.
2. Open a terminal in the project folder and run the application using Maven:
```bash
./mvnw spring-boot:run
```
The application will automatically create the required database tables when it starts up.

## API Endpoints

The system is split into two parts: HR endpoints (which require Basic Authentication) and public endpoints (for candidates to submit resumes).

HR Endpoints (Requires Auth):
- POST /admin/hr : Create a new HR user
- POST /admin/hr/login : Login
- POST /jobs : Post a new job description
- PATCH /jobs/{jobId}/close : Mark a job as closed
- GET /jobs/active : View open jobs
- GET /hr/{jobId}/top-candidates : View the best candidates for a specific job

Candidate Endpoints (Public):
- POST /apply/pdf : Submit a PDF resume (requires jobId, name, email, and the file)
- POST /apply/text : Submit a plain text resume

## How it Works

When a candidate uploads a PDF resume, the app uses PDFBox to read the text. It then asks the local Ollama embedding model to convert that text into vector data. After that, it feeds both the resume and the job description to the Llama 3 model, which returns an evaluation score, a shortlist decision, and a list of matched skills. Finally, everything gets saved to the PostgreSQL database.
