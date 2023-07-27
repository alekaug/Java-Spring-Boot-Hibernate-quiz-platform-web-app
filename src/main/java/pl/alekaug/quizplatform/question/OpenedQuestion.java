package pl.alekaug.quizplatform.question;

import pl.alekaug.quizplatform.answer.Answer;
import pl.alekaug.quizplatform.question.exceptions.ClosedQuestionHavingNoAnswers;
import pl.alekaug.quizplatform.question.exceptions.OpenedQuestionHavingAnswers;

import java.util.Collection;
import java.util.HashSet;

public class OpenedQuestion extends Question {
    public OpenedQuestion(String content) {
        setContent(content);
        setType(Type.OPEN);
    }
}
