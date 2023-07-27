package pl.alekaug.quizplatform.question;

import pl.alekaug.quizplatform.answer.Answer;

import java.util.Arrays;
import java.util.HashSet;

public class QuestionBuilder {
    private static final String OPEN = "open";
    private static final String SINGLE = "single";
    private static final String MULTI = "multi";

    private final Question question = new Question();


//    protected QuestionBuilder(String type) {
//        switch (type) {
//            case SINGLE:
//                question = new SingleClosedQuestion();
//                break;
//            case MULTI:
//                question = new MultiClosedQuestion();
//                break;
//            case OPEN:
//            default:
//                question = new OpenedQuestion();
//                break;
//        }
////        question.setId(null);
//    }

    protected QuestionBuilder(Question.Type type) {
        question.setType(type);
    }

    public QuestionBuilder id(Long id) {
        question.setId(id);
        return this;
    }

    public QuestionBuilder content(String content) {
        question.setContent(content);
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