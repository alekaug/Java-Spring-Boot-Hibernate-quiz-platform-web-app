package pl.alekaug.quizplatform.question.checker;

import pl.alekaug.quizplatform.answer.Answer;
import pl.alekaug.quizplatform.question.Question;
import pl.alekaug.quizplatform.question.checker.exceptions.DifferentAmountOfClosedQuestionsException;
import pl.alekaug.quizplatform.question.checker.exceptions.QuestionNotFoundException;
import pl.alekaug.quizplatform.question.checker.exceptions.QuestionsNotTheSameException;
import pl.alekaug.quizplatform.question.checker.exceptions.ScoreOutOfRangeException;

import java.util.Collection;
import java.util.Optional;

public abstract class AnswersChecker {
    public static float checkScore(Collection<Question> correctQuestions, Collection<Question> userClosedQuestions) throws DifferentAmountOfClosedQuestionsException,
            QuestionNotFoundException,
            ScoreOutOfRangeException,
            QuestionsNotTheSameException {
        float questionsAmount = correctQuestions.size();
        if (correctQuestions.size() < userClosedQuestions.size())
            throw new DifferentAmountOfClosedQuestionsException();
        float score = 0.f;
        for (Question uq : userClosedQuestions) {
            Optional<Question> cq = correctQuestions.stream().filter(q -> q.getId().equals(uq.getId())).findFirst();
            if (cq.isEmpty())
                throw new QuestionNotFoundException();
            score += checkIndividualScore(cq.get(), uq);
        }

        float finalScore = score / questionsAmount;
        if (finalScore > 1.f || finalScore < 0.f)
            throw new ScoreOutOfRangeException();
        return finalScore;
    }

    private static float checkIndividualScore(Question original, Question userQuestion)
            throws QuestionsNotTheSameException {
        if (!original.getId().equals(userQuestion.getId()))
            throw new QuestionsNotTheSameException();
        Collection<Answer> originalAnswers = original.getAnswers();
        Collection<Answer> userAnswers = userQuestion.getAnswers();

        // 1. Case: single closed
        if (Question.Type.SINGLE.equals(original.getType())) {
            // Get only correct original answer
            Optional<Answer> oneCorrectOriginalAnswer = originalAnswers.stream().filter(Answer::isCorrect).findFirst();
            // Get only correct user answer
            Optional<Answer> oneCorrectUserAnswer = userAnswers.stream().filter(Answer::isCorrect).findFirst();
            // Check for equality
            if (oneCorrectUserAnswer.isPresent() && oneCorrectOriginalAnswer.isPresent())
                return oneCorrectUserAnswer.get().getId().equals(oneCorrectOriginalAnswer.get().getId()) ? 1.f : 0.f;
            return 0.f;
        }

        // 2. Case: multi closed
        float allAmount = originalAnswers.size();
        int correctAmount = 0;
        for (Answer a : originalAnswers) {
            Optional<Answer> other = userAnswers.stream().filter(ans -> ans.getId().equals(a.getId())).findFirst();
            boolean ans = false;
            if (other.isPresent())
                ans = other.get().isCorrect();
            correctAmount += ans == a.isCorrect() ? 1 : 0;
        }
        return correctAmount / allAmount;
    }
}
