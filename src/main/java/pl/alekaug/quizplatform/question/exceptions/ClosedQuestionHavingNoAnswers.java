package pl.alekaug.quizplatform.question.exceptions;

public class ClosedQuestionHavingNoAnswers extends Exception {
    public ClosedQuestionHavingNoAnswers(String message) {
        super(message);
    }
}
