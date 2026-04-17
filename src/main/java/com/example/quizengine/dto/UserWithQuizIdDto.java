package com.example.quizengine.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserWithQuizIdDto {

    private Long userid;
    private String name;
    private Long quizId;
}
