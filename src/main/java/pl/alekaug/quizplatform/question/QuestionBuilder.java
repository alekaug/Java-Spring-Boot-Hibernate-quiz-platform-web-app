package pl.alekaug.quizplatform.question;

import pl.alekaug.quizplatform.answer.Answer;

import java.util.Arrays;
import java.util.HashSet;

public class QuestionBuilder {
    private final Question question;

    public QuestionBuilder() {
        question = new Question();
        question.setId(null);
    }

    public QuestionBuilder id(Long id) {
        question.setId(id);
        return this;
    }

    public QuestionBuilder content(String content) {
        question.setContent(content);
        return this;
    }

    public QuestionBuilder type(Integer type) {
        question.setType(type);
        return this;
    }

    public QuestionBuilder answer(Answer answer) {
        if (question.getAnswers() == null)
            question.setAnswers(new HashSet<>());
        question.getAnswers().add(answer);
        return this;
    }

    public QuestionBuilder answers(Answer... answers) {
        if (question.getAnswers() == null)
            question.setAnswers(new HashSet<>());
        question.getAnswers().addAll(Arrays.asList(answers));
        return this;
    }

    public Question build() {
        return this.question;
    }
}