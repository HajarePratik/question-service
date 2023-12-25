package com.hp.questionservice.repository;

import com.hp.questionservice.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question,Integer> {
    List<Question> findByCategory(String category);

    @Query(value = "SELECT q.id FROM question q where q.category=:category ORDER BY RAND() LIMIT :numQuestion",nativeQuery = true)
    List<Integer> findRandomQuestionByCategory(String category, int numQuestion);
}
