package com.example.quizengine.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDto {

    private Long questionId;

    private String question;

    private String option_A;
    private String option_B;
    private String option_C;
    private String option_D;

}
