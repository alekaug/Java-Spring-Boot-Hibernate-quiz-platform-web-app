package pl.alekaug.quizplatform.answer;

public class AnswerBuilder {
    private final Answer answer;

    public AnswerBuilder() {
        this.answer = new Answer();
    }

    public AnswerBuilder id(Long id) {
        answer.setId(id);
        return this;
    }

    public AnswerBuilder content(String content) {
        answer.setContent(content);
        return this;
    }

    public AnswerBuilder correct(Boolean correct) {
        answer.setCorrect(correct);
        return this;
    }

    public Answer build() {
        return answer;
    }
}
