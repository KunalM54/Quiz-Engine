package com.example.quizengine.repository;

import com.example.quizengine.model.QuizResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizResultRepository extends JpaRepository<QuizResult, Long> {
    QuizResult findByQuizSessionIdAndQuestionBankQuestionId(Long quizId, Long questionId);

    int countByQuizSessionId(Long quizId);

    int countByQuizSessionIdAndStatusTrue(Long quizId);
}
