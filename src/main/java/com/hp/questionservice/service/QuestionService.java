package com.hp.questionservice.service;

import com.hp.questionservice.entity.Question;
import com.hp.questionservice.entity.QuestionWrapper;
import com.hp.questionservice.entity.Response;
import com.hp.questionservice.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    @Autowired
    QuestionRepository questionRepository;

    public ResponseEntity<List<Question>> getAllQuestion() {
        try {
            return new ResponseEntity<>(questionRepository.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }


    public ResponseEntity<List<Question>> getQuestionByCategory(String category) {
        try {
            return new ResponseEntity<>(questionRepository.findByCategory(category), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> addQuestion(Question question) {
        try {
            questionRepository.save(question);
            return new ResponseEntity<>("Added Successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new String(), HttpStatus.BAD_REQUEST);
    }


    public ResponseEntity<String> updateQuestion(int id, Question question) {
        Optional<Question> questionOptional = questionRepository.findById(id);
        if (questionOptional.isPresent()) {
            Question newQuestion = questionOptional.get();
            newQuestion.setCategory(question.getCategory());
            newQuestion.setQuestionTitle(question.getQuestionTitle());
            newQuestion.setOption1(question.getOption1());
            newQuestion.setOption2(question.getOption2());
            newQuestion.setOption3(question.getOption3());
            newQuestion.setOption4(question.getOption4());
            newQuestion.setRightAnswer(question.getRightAnswer());
            newQuestion.setDifficultyLevel(question.getDifficultyLevel());
            questionRepository.save(newQuestion);
            return new ResponseEntity<>("Question Updated Successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Question not Found", HttpStatus.BAD_REQUEST);
        }
    }


    public ResponseEntity<String> deleteQuestion(int id) {
        try {
            questionRepository.deleteById(id);
            return new ResponseEntity<>("Question Delete Successfully", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Question not Found", HttpStatus.BAD_REQUEST);

    }

    public ResponseEntity<List<Integer>> getQuestionForQuiz(String category, int numQuestion) {
        List<Integer> questionList = questionRepository.findRandomQuestionByCategory(category, numQuestion);
        return new ResponseEntity<>(questionList, HttpStatus.OK);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuestionFromId(List<Integer> questionIds) {

        List<QuestionWrapper> questionWrappers = new ArrayList<>();
        List<Question> questions = new ArrayList<>();

        for (Integer id : questionIds) {
            questions.add(questionRepository.findById(id).get());
        }

        for (Question q : questions) {
            QuestionWrapper wrapper = new QuestionWrapper();
            wrapper.setId(q.getId());
            wrapper.setQuestionTitle(q.getQuestionTitle());
            wrapper.setOption1(q.getOption1());
            wrapper.setOption2(q.getOption2());
            wrapper.setOption3(q.getOption3());
            wrapper.setOption4(q.getOption4());
            questionWrappers.add(wrapper);
        }
        return new ResponseEntity<>(questionWrappers, HttpStatus.OK);
    }

    public ResponseEntity<Integer> getScore(List<Response> responses) {

        int rightAnswer = 0;
        for (Response response : responses) {
            Question question = questionRepository.findById(response.getId()).get();
            if (response.getResponse().equals(question.getRightAnswer())) {
                rightAnswer++;
            }
        }
        return new ResponseEntity<>(rightAnswer, HttpStatus.OK);
    }
}
