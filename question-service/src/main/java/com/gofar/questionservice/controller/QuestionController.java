package com.gofar.questionservice.controller;


import com.gofar.questionservice.exception.QuestionException;
import com.gofar.questionservice.model.Question;
import com.gofar.questionservice.service.QuestionService;
import com.gofar.questionservice.utils.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("question")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("all")
    public ResponseEntity<Response> getAllQuestions(){
        List<Question> questions = questionService.getAllQuestions();
        if (questions.isEmpty()) {
            return new ResponseEntity<>(
                    Response.builder()
                            .data(null)
                            .message("No question found").build(),
                    HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(
                Response.builder()
                        .data(questions)
                        .message("Questions retrieved successfully").build(),
                HttpStatus.OK);
    }

    @GetMapping("category/{category}")
    public ResponseEntity<Response> getQuestionsByCategory(@PathVariable String category){
        List<Question> questionsByCategory = questionService.getQuestionsByCategory(category);
        if (questionsByCategory.isEmpty()) {
            return new ResponseEntity<>(
                    Response.builder()
                            .data(null)
                            .message(String.format("No question of category %s found", category)).build(),
                    HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(
                Response.builder()
                        .data(questionsByCategory)
                        .message(String.format("Questions of category %s retrieved successfully", category)).build(),
                HttpStatus.OK);
    }

    @PostMapping("add")
    public ResponseEntity<Response> addQuestion(@RequestBody Question question){
        Question savedQuestion;
        try {
            savedQuestion = questionService.addQuestion(question);
        } catch (QuestionException e) {
            return new ResponseEntity<>(
                    Response.builder()
                            .data(null)
                            .message(e.getMessage()).build(),
                    HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(
                Response.builder()
                        .data(savedQuestion)
                        .message("Question created successfully")
                        .build(),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/generate")
    public ResponseEntity<Response> getQuizQuestionsIds(@RequestParam("category") String category,
                                                     @RequestParam("nums") Integer nums) {
        return new ResponseEntity<>(Response.builder()
                .data(questionService.getRandomQuestionsIds(category, nums))
                .message("Questions ids retrieved successfully")
                .build(),
                HttpStatus.OK);
    }

    @PostMapping("/quiz-questions")
    public ResponseEntity<Response> quizQuestions(@RequestBody List<Integer> ids) {
        return new ResponseEntity<>(Response.builder()
                .data(questionService.getQuestions(ids))
                .message("Questions retrieved successfully").build(),
                HttpStatus.OK);
    }

    @PostMapping("/result")
    public ResponseEntity<Response> calculateResult(@RequestBody List<com.gofar.questionservice.model.Response> responses) {
        return new ResponseEntity<>(Response.builder()
                .data(questionService.calculateResult(responses))
                .message("Quiz result calculated successfully")
                .build(),
                HttpStatus.OK);
    }
}
