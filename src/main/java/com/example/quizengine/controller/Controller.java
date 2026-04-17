package com.example.quizengine.controller;

import com.example.quizengine.dto.QuestionDto;
import com.example.quizengine.dto.SendQuestionDto;
import com.example.quizengine.dto.UserWithQuizIdDto;
import com.example.quizengine.model.User;
import com.example.quizengine.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quiz")
public class Controller {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public UserWithQuizIdDto register(@RequestBody User user){
        return userService.generateQuizId(user);
    }

    @PostMapping("/question/userId/{userId}/quizId/{quizId}")
    public QuestionDto getQuestion(@PathVariable Long userId,
                                   @PathVariable Long quizId) {
        return userService.getQuestion(userId,quizId);
    }

    @GetMapping("/answer")
    public String sendAnswer(@RequestBody SendQuestionDto sendQuestionDto) {
        return userService.sendAnswer(sendQuestionDto);
    }


}
