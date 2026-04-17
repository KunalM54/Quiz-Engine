package com.example.quizengine.service;

import com.example.quizengine.dto.QuestionDto;
import com.example.quizengine.dto.SendQuestionDto;
import com.example.quizengine.dto.UserWithQuizIdDto;
import com.example.quizengine.model.QuestionBank;
import com.example.quizengine.model.QuizResult;
import com.example.quizengine.model.User;
import com.example.quizengine.model.QuizSession;
import com.example.quizengine.repository.QuestionBankRepository;
import com.example.quizengine.repository.QuizResultRepository;
import com.example.quizengine.repository.QuizSessionRepository;
import com.example.quizengine.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionBankRepository questionBankRepository;

    @Autowired
    private QuizSessionRepository quizSessionRepository;

    @Autowired
    private QuizResultRepository quizResultRepository;

    public UserWithQuizIdDto generateQuizId(User user) {
        User userObj = userRepository.save(user);

        QuizSession session = new QuizSession();
        session.setUser(userObj);

        QuizSession savedSession = quizSessionRepository.save(session);

        return new UserWithQuizIdDto(
                userObj.getUserId(),
                userObj.getName(),
                savedSession.getId()
        );
    }

    public QuestionDto getQuestion(Long userId, Long quizId) {

        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        QuizSession quizSession = quizSessionRepository
                .findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz session not found"));

        // stop after 5 questions
        if (quizSession.getTotalQuestion() >= 5) {
            throw new RuntimeException("Quiz already completed");
        }

        QuestionBank question = questionBankRepository
                .getNextRandomQuestion(quizSession.getId());

        if (question == null) {
            throw new RuntimeException("No more questions available");
        }

        // save asked question
        QuizResult quizResult = new QuizResult();
        quizResult.setQuizSession(quizSession);
        quizResult.setUser(user);
        quizResult.setQuestionBank(question);

        quizResultRepository.save(quizResult);

        // increment question counter
        quizSession.setTotalQuestion(quizSession.getTotalQuestion() + 1);
        quizSessionRepository.save(quizSession);

        // convert to DTO
        QuestionDto questionDto = new QuestionDto();
        questionDto.setQuestionId(question.getQuestionId());
        questionDto.setQuestion(question.getQuestion());
        questionDto.setOption_A(question.getOptionA());
        questionDto.setOption_B(question.getOptionB());
        questionDto.setOption_C(question.getOptionC());
        questionDto.setOption_D(question.getOptionD());

        return questionDto;
    }

    public String sendAnswer(SendQuestionDto dto) {

        QuizResult quizResult = quizResultRepository
                .findByQuizSessionIdAndQuestionBankQuestionId(
                        dto.getQuizId(),
                        dto.getQuestionId()
                );

        if (quizResult == null) {
            throw new RuntimeException("Question not found in quiz session");
        }

        QuizSession session = quizResult.getQuizSession();

        QuestionBank question = quizResult.getQuestionBank();

        String correctAnswer = question.getCorrectAnswer();

        quizResult.setSubmittedAnswer(dto.getAnswer());

        boolean correct = correctAnswer.equalsIgnoreCase(dto.getAnswer());
        quizResult.setStatus(correct);

        quizResultRepository.save(quizResult);

        // increment total answers in session
        session.setTotalAnswer(session.getTotalAnswer() + 1);
        quizSessionRepository.save(session);

        String response;

        if (correct) {
            response = "Correct Answer";
        } else {
            response = "Incorrect Answer. Correct option is: " + correctAnswer;
        }

        // if quiz finished (5 answers)
        if (session.getTotalAnswer() == 5) {

            int total = quizResultRepository.countByQuizSessionId(dto.getQuizId());
            int correctAnswers = quizResultRepository.countByQuizSessionIdAndStatusTrue(dto.getQuizId());
            int wrongAnswers = total - correctAnswers;

            return response +
                    "\n\nQuiz Completed!" +
                    "\nTotal Questions: " + total +
                    "\nCorrect Answers: " + correctAnswers +
                    "\nWrong Answers: " + wrongAnswers;
        }
        return response;
    }
}
