package pl.alekaug.quizplatform.question;

import pl.alekaug.quizplatform.answer.Answer;

import java.util.Collection;

public class MultiClosedQuestion extends Question {
    public MultiClosedQuestion(String content, Collection<Answer> answers) {
        setContent(content);
        setType(Type.MULTI);
        setAnswers(answers);
    }
}
