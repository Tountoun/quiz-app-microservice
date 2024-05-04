package com.gofar.quizservice.utils;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuizWrapper {
    private Integer id;
    private String title;
}
