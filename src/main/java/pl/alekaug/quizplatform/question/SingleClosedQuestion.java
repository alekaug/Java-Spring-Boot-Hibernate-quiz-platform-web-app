package pl.alekaug.quizplatform.question;

import pl.alekaug.quizplatform.answer.Answer;

import java.util.Collection;

public class SingleClosedQuestion extends Question {
    public SingleClosedQuestion(String content, Collection<Answer> answers) {
        setContent(content);
        setType(Type.SINGLE);
        setAnswers(answers);
    }
}
