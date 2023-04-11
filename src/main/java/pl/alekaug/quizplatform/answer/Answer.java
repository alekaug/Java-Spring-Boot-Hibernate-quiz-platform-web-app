package pl.alekaug.quizplatform.answer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import static pl.alekaug.quizplatform.constants.DatabaseConstants.*;

@Entity
@Table(name = ANSWERS_TABLE)
@Data public class Answer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ANSWER_ID_COLUMN, nullable = false)
    @JsonIgnore
    private Long id;

    @Column(name = ANSWER_CONTENT_COLUMN, nullable = false)
    private String content;

    @Column(name = ANSWER_IS_CORRECT_COLUMN, nullable = false)
    private boolean isCorrect;

    public Answer() { }

    public Answer(String content, boolean isCorrect) {
        this.content = content;
        this.isCorrect = isCorrect;
    }

    public static AnswerBuilder builder() {
        return new AnswerBuilder();
    }

    public void changeTo(Answer other) {
        this.content = other.getContent();
        this.isCorrect = other.isCorrect();
    }
}
