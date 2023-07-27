package pl.alekaug.quizplatform.question;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import pl.alekaug.quizplatform.answer.Answer;
import pl.alekaug.quizplatform.question.exceptions.ClosedQuestionHavingNoAnswers;
import pl.alekaug.quizplatform.question.exceptions.OpenedQuestionHavingAnswers;
import pl.alekaug.quizplatform.resource.Resource;

import java.util.Collection;
import java.util.HashSet;

import static pl.alekaug.quizplatform.constants.DatabaseConstants.*;

@Entity
@Table(name = QUESTIONS_TABLE)
@Data public class Question {
    public enum Type {
        OPEN,
        SINGLE,
        MULTI
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = QUESTION_ID_COLUMN, nullable = false)
    @JsonIgnore
    private Long id;

    @Column(name = QUESTION_CONTENT_COLUMN, nullable = false)
    @NotBlank
    private String content;
    @Column(name = QUESTION_TYPE_COLUMN, nullable = false)
    private Type type;

    @OneToMany
    @JoinColumn(name=QUESTION_ID_COLUMN, nullable = false)
    private Collection<Answer> answers;

    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name="q_id", referencedColumnName = "r_id")
    private Resource resource;

    public Question() { }

    public Question(String content, Type type, Collection<Answer> answers)
            throws ClosedQuestionHavingNoAnswers, OpenedQuestionHavingAnswers {
//        if (type.equals(Type.OPEN) && !answers.isEmpty())
//            throw new OpenedQuestionHavingAnswers("No answers allowed in opened-type questions.");
//        else if (type.equals(Type.SINGLE) || type.equals(Type.MULTI) && answers.isEmpty())
//            throw new ClosedQuestionHavingNoAnswers("The are no answers provided for closed-type question.");
        this.content = content;
        this.type = type;
        this.answers = new HashSet<>(answers);
    }

    public void changeTo(Question other) {
        this.content = other.getContent();
        this.type = other.getType();
        this.answers = other.getAnswers();
    }

    /**
     * Available question builder types:
     * <ul>
     *     <li>{@code open} – Open question</li>
     *     <li>{@code single} – Singly-closed question</li>
     *     <li>{@code multi} – Multi-closed question</li>
     * </ul>
     *
     * @param type Type of question in enum value
     */
    public static QuestionBuilder builder(Type type) {
        return new QuestionBuilder(type);
    }

}