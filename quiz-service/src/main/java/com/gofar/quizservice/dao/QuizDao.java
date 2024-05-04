package com.gofar.quizservice.dao;

import com.gofar.quizservice.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizDao extends JpaRepository<Quiz,Integer> {

    boolean existsByTitle(String title);

    Quiz findQuizById(Integer id);
}
