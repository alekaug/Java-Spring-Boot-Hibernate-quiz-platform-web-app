package pl.alekaug.quizplatform.answer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswersRepository extends JpaRepository<Answer, Long> {
    @Modifying
    @Query(value = "truncate answers restart identity cascade;", nativeQuery = true)
    Integer deleteAllReturnNumber();
}
