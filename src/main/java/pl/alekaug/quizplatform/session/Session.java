package pl.alekaug.quizplatform.session;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

import static pl.alekaug.quizplatform.constants.DatabaseConstants.*;

@Entity
@Table(name = SESSIONS_TABLE)
@Data public class Session {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = SESSION_ID_COLUMN, nullable = false)
    @JsonIgnore
    private Long id;

    @Column(name = SESSION_USERNAME_COLUMN, nullable = false)
    private String username;

    @Column(name = SESSION_TIME_STARTED_COLUMN)
    private Timestamp timeStarted;

    @Column(name = SESSION_TIME_FINISHED_COLUMN)
    private Timestamp timeFinished;

    public Session() {  }

    public Session(String username, Timestamp timeStarted, Timestamp timeFinished) {
        this.username = username;
        this.timeStarted = timeStarted;
        this.timeFinished = timeFinished;
    }
}
