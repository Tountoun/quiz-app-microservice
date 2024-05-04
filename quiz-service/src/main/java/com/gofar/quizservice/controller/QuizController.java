package com.gofar.quizservice.controller;

import com.gofar.quizservice.dto.QuizDto;
import com.gofar.quizservice.exception.QuizException;
import com.gofar.quizservice.service.QuizService;
import com.gofar.quizservice.utils.QuestionWrapper;
import com.gofar.quizservice.utils.QuizWrapper;
import com.gofar.quizservice.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("quiz")
@Slf4j
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping("create")
    public ResponseEntity<Response> createQuiz(@RequestBody QuizDto dto){
        QuizWrapper quizWrapper = null;
        try {
            quizWrapper = quizService.createQuiz(dto.getCategoryName(), dto.getNumQuestions(), dto.getTitle());
        } catch (QuizException e) {
            return new ResponseEntity<>(Response.builder()
                    .message(e.getMessage())
                    .build(),
                    HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(Response.builder()
                .data(quizWrapper)
                .message("Quiz created successfully")
                .build(),
                HttpStatus.CREATED);
    }

    @GetMapping("get/{id}")
    public ResponseEntity<Response> getQuizQuestions(@PathVariable Integer id){
        try {
            List<QuestionWrapper> quizQuestions = quizService.getQuizQuestions(id);
            return new ResponseEntity<>(Response.builder()
                    .data(quizQuestions)
                    .message(String.format("Questions of quiz with id %s retrieved successfully", id))
                    .build(),
                    HttpStatus.OK);
        } catch (QuizException e) {
            return new ResponseEntity<>(Response.builder()
                    .message(e.getMessage())
                    .build(),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("submit/{id}")
    public ResponseEntity<Response> submitQuiz(@PathVariable Integer id, @RequestBody List<com.gofar.quizservice.model.Response> responses){
        Integer mark = quizService.calculateResult(id, responses);
        return new ResponseEntity<>(Response.builder()
                .data(mark)
                .message("Quiz result calculated successfully")
                .build(),
                HttpStatus.OK);
    }


}
