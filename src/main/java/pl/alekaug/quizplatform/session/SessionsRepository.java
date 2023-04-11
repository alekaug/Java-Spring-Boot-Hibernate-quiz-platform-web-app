package pl.alekaug.quizplatform.session;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SessionsRepository extends JpaRepository<Session, Long> {
    @Modifying
    @Query(value = "delete from sessions;", nativeQuery = true)
    Integer deleteAllReturnNumber();
}
