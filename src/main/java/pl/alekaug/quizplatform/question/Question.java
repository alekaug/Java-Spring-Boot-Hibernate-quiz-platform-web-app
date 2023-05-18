package pl.alekaug.quizplatform.question;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import pl.alekaug.quizplatform.answer.Answer;
import pl.alekaug.quizplatform.question.exceptions.ClosedQuestionHavingNoAnswers;
import pl.alekaug.quizplatform.question.exceptions.OpenedQuestionHavingAnswers;
import pl.alekaug.quizplatform.resource.Resource;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static pl.alekaug.quizplatform.constants.DatabaseConstants.*;

@Entity
@Table(name = QUESTIONS_TABLE)
@Data public class Question {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = QUESTION_ID_COLUMN, nullable = false)
    @JsonIgnore
    private Long id;

    @Column(name = QUESTION_CONTENT_COLUMN, nullable = false)
    private String content;
    @Column(name = QUESTION_TYPE_COLUMN, nullable = false)
    private Integer type;

    @OneToMany
    @JoinColumn(name=QUESTION_ID_COLUMN, nullable = false)
    private Set<Answer> answers;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="q_id", referencedColumnName = "r_id")
    private Resource resource;

    public Question() { }

    public Question(String content, Integer type, Collection<Answer> answers)
            throws ClosedQuestionHavingNoAnswers, OpenedQuestionHavingAnswers {
        this.content = content;
        this.type = type;
        if (type == 0 && !answers.isEmpty())
            throw new OpenedQuestionHavingAnswers("No answers allowed in opened-type questions.");
        else if (type == 1 || type == 2 && answers.isEmpty())
            throw new ClosedQuestionHavingNoAnswers("The are no answers provided for closed-type question.");
        this.answers = new HashSet<>(answers);
    }

    public void changeTo(Question other) {
        this.content = other.getContent();
        this.type = other.getType();
        this.answers = other.getAnswers();
    }

    public static QuestionBuilder builder() {
        return new QuestionBuilder();
    }

}