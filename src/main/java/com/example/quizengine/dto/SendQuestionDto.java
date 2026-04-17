package com.example.quizengine.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SendQuestionDto {
    private Long quizId;
    private Long questionId;
    private String answer;
}
