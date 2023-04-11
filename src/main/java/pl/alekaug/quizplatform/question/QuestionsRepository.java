package pl.alekaug.quizplatform.question;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionsRepository extends JpaRepository<Question, Long> {
    @Modifying
    @Query(value = "delete from questions;", nativeQuery = true)
    Integer deleteAllReturnNumber();
}
