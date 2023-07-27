package pl.alekaug.quizplatform.question.checker;

import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import pl.alekaug.quizplatform.answer.Answer;
import pl.alekaug.quizplatform.answer.AnswersService;
import pl.alekaug.quizplatform.question.Question;
import pl.alekaug.quizplatform.question.QuestionsService;
import pl.alekaug.quizplatform.question.checker.exceptions.*;

import java.util.*;

@Service
public class AnswersCheckerService {
    private final QuestionsService questionsService;
    private final AnswersService answersService;

    public AnswersCheckerService(QuestionsService questionsService, AnswersService answersService) {
        this.questionsService = questionsService;
        this.answersService = answersService;
    }

    public float checkAnswers(MultiValueMap<String, String> userQuestions)
            throws QuestionNotFoundException,
            QuestionsNotTheSameException,
            ScoreOutOfRangeException,
            DifferentAmountOfClosedQuestionsException, AnswerNotFoundException {
        final List<Question> correctQuestions = questionsService.getAllClosed();
        if (correctQuestions.size() == 0) return 1.f;
        final List<Question> userClosedQuestions = formToClosedQuestions(userQuestions);
        return AnswersChecker.checkScore(correctQuestions, userClosedQuestions);
    }

    private List<Question> formToClosedQuestions(MultiValueMap<String, String> userForm) throws QuestionNotFoundException, AnswerNotFoundException {
        final List<Question> userQuestions = new LinkedList<>();

        // Case 1: question does not exist in the form, so it's cloned and its answers are zeroed
        for (Map.Entry<String, List<String>> entry : userForm.entrySet()) {
            Optional<Question> foundQuestion = questionsService.getById(Long.parseLong(entry.getKey()));
            if (foundQuestion.isEmpty())
                throw new QuestionNotFoundException();
            Question.Type foundQuestionType = foundQuestion.get().getType();
            // Reduce set by open questions
            if (foundQuestionType.equals(Question.Type.OPEN))
                continue;
            Collection<String> v = entry.getValue();
            Collection<Answer> answers = new ArrayList<>(v.size());
            for (String l : v) {
                Optional<Answer> foundAnswer = answersService.getById(Long.parseLong(l));
                if (foundAnswer.isEmpty())
                    throw new AnswerNotFoundException();
                Answer aOriginal = foundAnswer.get();
                Answer aClone = Answer.builder()
                        .id(aOriginal.getId())
                        .correct(aOriginal.isCorrect())
                        .build();
                aClone.setCorrect(true);
                answers.add(aClone);
            }
            Answer[] answersArr = answers.toArray(new Answer[0]);
            Question newQuestion = Question.builder(foundQuestionType)
                    .id(Long.parseLong(entry.getKey()))
                    .answers(answersArr)
                    .build();
            userQuestions.add(newQuestion);
        }
        return userQuestions;
    }
}
