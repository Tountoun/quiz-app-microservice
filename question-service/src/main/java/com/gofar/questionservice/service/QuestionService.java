package com.gofar.questionservice.service;

import com.gofar.questionservice.dao.QuestionDao;
import com.gofar.questionservice.exception.QuestionException;
import com.gofar.questionservice.model.Question;
import com.gofar.questionservice.model.QuestionWrapper;
import com.gofar.questionservice.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class QuestionService {

    private final QuestionDao questionDao;

    public QuestionService(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    public List<Question> getAllQuestions() {
        return this.questionDao.findAll();
    }

    public List<Question> getQuestionsByCategory(String category) {
        return this.questionDao.findByCategoryIgnoreCase(category);
    }

    public Question addQuestion(Question question) {
        if (questionDao.existsByQuestionTitle(question.getQuestionTitle())) {
            throw new QuestionException("Question with title " + question.getQuestionTitle() + " already exists");
        }
        return questionDao.save(question);
    }

    public List<Integer> getRandomQuestionsIds(String categoryName, int numberOfQuestions) {
        List<Question> randomQuestionsByCategory = questionDao.findRandomQuestionsByCategory(categoryName, numberOfQuestions);
        return randomQuestionsByCategory
                .stream().map(Question::getId).toList();
    }

    public List<QuestionWrapper> getQuestions(List<Integer> questionIds) {
        List<Question> questions = questionDao.findAllById(questionIds);
        return questions.stream().map(
                question ->
                    QuestionWrapper.builder()
                            .id(question.getId())
                            .questionTitle(question.getQuestionTitle())
                            .option1(question.getOption1())
                            .option2(question.getOption2())
                            .option3(question.getOption3())
                            .option4(question.getOption4()).build()
        ).toList();
    }

    public Integer calculateResult(List<Response> responses) {
        int right = 0;
        for(Response response : responses){
            Optional<Question> optionalQuestion = questionDao.findById(response.getId());
            if (optionalQuestion.isPresent()) {
                if(response.getResponse().equals(optionalQuestion.get().getRightAnswer()))
                    right++;
            } else {
                log.info("Question with id {} not found", response.getId());
            }
        }
        return right;
    }
}
