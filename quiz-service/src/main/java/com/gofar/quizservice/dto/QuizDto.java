package com.gofar.quizservice.dto;

import lombok.Data;

@Data
public class QuizDto {
    private String category;
    private int numberOfQuestions;
    private String title;
}
