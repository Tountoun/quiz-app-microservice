package com.gofar.questionservice.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "question")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "question_id_generator")
    @SequenceGenerator(name = "question_id_generator", sequenceName = "question_sequence", allocationSize = 2)
    private Integer id;
    @Column(name = "title", unique = true, nullable = false)
    private String questionTitle;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    @Column(name = "answer")
    private String rightAnswer;
    @Column(name = "level")
    private String difficultyLevel;
    private String category;


}
