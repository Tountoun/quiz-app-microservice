package com.gofar.quizservice.service;

import com.gofar.quizservice.config.QuizInterface;
import com.gofar.quizservice.dao.QuizDao;
import com.gofar.quizservice.exception.QuizException;
import com.gofar.quizservice.model.Quiz;
import com.gofar.quizservice.model.Response;
import com.gofar.quizservice.utils.QuestionWrapper;
import com.gofar.quizservice.utils.QuizWrapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizService {


    private final QuizDao quizDao;
    private final QuizInterface quizInterface;

    public QuizService(QuizDao quizDao, QuizInterface quizInterface) {
        this.quizDao = quizDao;
        this.quizInterface = quizInterface;
    }

    public QuizWrapper createQuiz(String category, int numQ, String title) {
        if (quizDao.existsByTitle(title)) {
            throw new QuizException("Quiz with title " + title + " already exists");
        }
        com.gofar.quizservice.utils.Response response = quizInterface.getQuizQuestionsIds(category, numQ).getBody();
        assert response != null;
        List<Integer> questions = (List<Integer>) response.getData();

        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestions(questions);
        quiz = quizDao.save(quiz);
        return QuizWrapper.builder().id(quiz.getId()).title(quiz.getTitle()).build();
    }

    public List<QuestionWrapper> getQuizQuestions(Integer id) {
        if (!quizDao.existsById(id)) {
            throw new QuizException("Quiz with id " + id + " does not exist");
        }
        Quiz quiz = quizDao.findQuizById(id);
        com.gofar.quizservice.utils.Response response = quizInterface.quizQuestions(quiz.getQuestions()).getBody();
        assert response != null;
        return (List<QuestionWrapper>) response.getData();
    }

    public Integer calculateResult(Integer id, List<Response> responses) {
        if (!quizDao.existsById(id)) {
            throw new QuizException("Quiz with id " + id + " does not exist");
        }
        com.gofar.quizservice.utils.Response response = quizInterface.calculateResult(responses).getBody();
        assert response != null;
        return (Integer) response.getData();
    }
}
