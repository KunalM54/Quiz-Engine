# Quiz Engine

A Spring Boot REST API for conducting interactive quizzes with random question selection, answer validation, and session-based tracking.

## Overview

This application manages quiz sessions where users register to take quizzes. It selects questions randomly from a question bank, tracks user answers, validates responses, and provides results upon completion. Each quiz session is limited to 5 questions per user.

## Features

* User Registration with Quiz Session Creation
* Random Question Selection from Question Bank
* Answer Validation
* Session-Based Quiz Tracking
* Quiz Completion Summary

## Tech Stack

* Java 17
* Spring Boot 4.0.3
* Spring Data JPA
* Spring Web MVC
* MySQL
* Lombok

## System Design / How It Works

1. User registers via `/quiz/register` - creates User and QuizSession, returns userId and quizId
2. User requests next question via `/quiz/question/{userId}/{quizId}` - fetches random question not yet asked, increments totalQuestion counter
3. Quiz is limited to 5 questions per session
4. User submits answer via `/quiz/answer` with quizId, questionId, and answer
5. System validates answer against correctAnswer, saves result with status (correct/incorrect), increments totalAnswer counter
6. After 5 answers submitted, response includes quiz summary with total questions, correct answers, and wrong answers

## Project Structure

```text
com.example.quizengine
├── QuizApplication.java
├── controller
│   └── Controller.java
├── service
│   └── UserService.java
├── repository
│   ├── UserRepository.java
│   ├── QuestionBankRepository.java
│   ├── QuizSessionRepository.java
│   └── QuizResultRepository.java
├── model
│   ├── User.java
│   ├── QuestionBank.java
│   ├── QuizSession.java
│   └── QuizResult.java
└── dto
    ├── QuestionDto.java
    ├── UserWithQuizIdDto.java
    └── SendQuestionDto.java
```

## Setup & Installation

1. Ensure Java 17 and Maven are installed
2. Create MySQL database named `quiz_db`
3. Configure `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/quiz_db
   spring.datasource.username=root
   spring.datasource.password=your_password
   ```
4. Run `mvn spring-boot:run`

## API Endpoints

Base path: `http://localhost:8080/quiz`

### 1) Register User

* **POST** `/quiz/register`
* **Request Body**: User object

```http
POST /quiz/register
{"name":"John"}
```

### 2) Get Next Question

* **POST** `/quiz/question/userId/{userId}/quizId/{quizId}`
* **Path Variables**: userId, quizId

```http
POST /quiz/question/userId/1/quizId/1
```

### 3) Submit Answer

* **GET** `/quiz/answer`
* **Request Body**: SendQuestionDto

```http
GET /quiz/answer
{"quizId":1,"questionId":1,"answer":"A"}
```

## Database Schema

### `user`

* `user_id` (PK)
* `name`

### `question_bank`

* `question_id` (PK)
* `question`
* `optionA`
* `optionB`
* `optionC`
* `optionD`
* `correctAnswer`

### `quiz_session`

* `id` (PK)
* `user_id` (FK -> user)
* `totalQuestion`
* `totalAnswer`

### `quiz_result`

* `id` (PK)
* `quiz_id` (FK -> quiz_session)
* `user_id` (FK -> user)
* `question_id` (FK -> question_bank)
* `submittedAnswer`
* `status`

## Configuration Notes

* `spring.application.name=quiz-engine`
* `spring.datasource.url=jdbc:mysql://localhost:3306/quiz_db`
* `spring.datasource.username=root`
* `spring.datasource.password=your_password`
* `spring.jpa.hibernate.ddl-auto=update`

## Future Improvements

* Add timer for each question
* Implement score-based leaderboard
* Add category/topic-based question filtering
* Support multiple quiz types
