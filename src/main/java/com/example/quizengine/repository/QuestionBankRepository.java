package com.example.quizengine.repository;

import com.example.quizengine.model.QuestionBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionBankRepository extends JpaRepository<QuestionBank, Long> {

    @Query(value = """
        SELECT * FROM question_bank WHERE question_id NOT IN (
        SELECT question_id FROM quiz_result WHERE quiz_id = :quizId)
        ORDER BY RAND() LIMIT 1
        """, nativeQuery = true)
    QuestionBank getNextRandomQuestion(@Param("quizId") Long quizId);

}
