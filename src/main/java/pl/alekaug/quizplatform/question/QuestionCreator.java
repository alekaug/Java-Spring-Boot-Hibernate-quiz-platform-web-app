package pl.alekaug.quizplatform.question;

import pl.alekaug.quizplatform.answer.Answer;

import java.util.Collection;

public abstract class QuestionCreator {
    static Question createQuestion(String content, Collection<Answer> answers) {
        int answersSize = answers.size();
        if (answersSize == 0)
            return new OpenedQuestion(content);
        else if (answersSize == 1)
            return new SingleClosedQuestion(content, answers);
        else
            return new MultiClosedQuestion(content, answers);
    }
}
