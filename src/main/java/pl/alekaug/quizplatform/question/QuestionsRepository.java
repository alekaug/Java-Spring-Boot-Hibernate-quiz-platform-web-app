package pl.alekaug.quizplatform.question;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionsRepository extends JpaRepository<Question, Long> {
    @Modifying
    @Query(value = "truncate table questions restart identity cascade;", nativeQuery = true)
    Integer deleteAllReturnNumber();

    @Query(
            value = "select * from questions where question_type = 1 or question_type = 2",
            nativeQuery = true
    )
    List<Question> getAllClosed();
}
