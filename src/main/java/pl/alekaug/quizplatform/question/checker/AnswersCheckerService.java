package pl.alekaug.quizplatform.question.checker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import pl.alekaug.quizplatform.answer.Answer;
import pl.alekaug.quizplatform.answer.AnswersService;
import pl.alekaug.quizplatform.question.Question;
import pl.alekaug.quizplatform.question.QuestionsService;

import java.util.*;

@Service
public class AnswersCheckerService {
    @Autowired
    private QuestionsService questionsService;
    @Autowired
    private AnswersService answersService;

    public float checkAnswers(MultiValueMap<String, String> userQuestions)
            throws QuestionNotFoundException,
            QuestionsNotTheSameException,
            ScoreOutOfRangeException,
            DifferentAmountOfClosedQuestionsException {
        final List<Question> correctQuestions = questionsService.getAllClosed();
        final List<Question> userClosedQuestions = translateFormToClosedQuestions(userQuestions);
        float questionsAmount = correctQuestions.size();
        if (correctQuestions.size() < userClosedQuestions.size())
            throw new DifferentAmountOfClosedQuestionsException();
        float score = 0.f;

        for (Question uq : userClosedQuestions) {
            Optional<Question> cq = correctQuestions.stream().filter(q -> q.getId().equals(uq.getId())).findFirst();
            if (cq.isEmpty())
                throw new QuestionNotFoundException();
            score += getScore(cq.get(), uq);
        }

        float finalScore = score / questionsAmount;
        if (finalScore > 1.f || finalScore < 0.f)
            throw new ScoreOutOfRangeException();
        return finalScore;
    }

    private List<Question> translateFormToClosedQuestions(MultiValueMap<String, String> userForm) {
        final List<Question> userQuestions = new LinkedList<>();

        // Case 1: question does not exist in the form, so it's cloned and its answers are zeroed
        for (Map.Entry<String, List<String>> entry : userForm.entrySet()) {
            // Reduce set by open questions
            if (questionsService.getById(Long.parseLong(entry.getKey())).get().getType() == 0)
                continue;
            List<String> v = entry.getValue();
            List<Answer> answers = new ArrayList<>(v.size());
            for (String l : v) {
                Answer a = answersService.getById(Long.parseLong(l)).get();
                Answer clone = Answer.builder()
                        .id(a.getId())
                        .correct(a.isCorrect())
                        .build();
                clone.setCorrect(true);
                answers.add(clone);
            }
            Answer[] answersArr = answers.toArray(new Answer[0]);
            Question newQuestion = Question.builder()
                    .id(Long.parseLong(entry.getKey()))
                    .answers(answersArr)
                    .build();
            userQuestions.add(newQuestion);
        }
        return userQuestions;
    }

    private float getScore(Question original, Question userQuestion)
            throws QuestionsNotTheSameException {
        if (!original.getId().equals(userQuestion.getId()))
            throw new QuestionsNotTheSameException();
        Set<Answer> originalAnswers = original.getAnswers();
        Set<Answer> userAnswers = userQuestion.getAnswers();

        // 1. Case: single closed
        if (original.getType() == 1) {
            // Get only correct original answer
            Optional<Answer> oneCorrectOriginalAnswer = originalAnswers.stream().filter(Answer::isCorrect).findFirst();
            // Get only correct user answer
            Optional<Answer> oneCorrectUserAnswer = userAnswers.stream().filter(Answer::isCorrect).findFirst();
            // Check if equals to each other
            if (oneCorrectUserAnswer.isPresent() && oneCorrectOriginalAnswer.isPresent())
                return oneCorrectUserAnswer.get().getId().equals(oneCorrectOriginalAnswer.get().getId()) ? 1.f : 0.f;
            return 0.f;
        }

        // 2. Case: multi closed
        float allAmount = originalAnswers.size();
        int correctAmount = 0;
        //TODO:HERE
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
