package com.gofar.quizservice.config;


import com.gofar.quizservice.utils.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "QUESTION-SERVICE")
public interface QuizInterface {

    @GetMapping("question/generate")
    public ResponseEntity<Response> getQuizQuestionsIds(@RequestParam("category") String category,
                                                        @RequestParam("nums") Integer nums);

    @PostMapping("question/quiz-questions")
    public ResponseEntity<Response> quizQuestions(@RequestBody List<Integer> ids);

    @PostMapping("question/result")
    public ResponseEntity<Response> calculateResult(@RequestBody List<com.gofar.quizservice.model.Response> responses);
}
