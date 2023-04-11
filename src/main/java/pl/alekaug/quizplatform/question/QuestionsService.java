package pl.alekaug.quizplatform.question;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.alekaug.quizplatform.answer.Answer;
import pl.alekaug.quizplatform.answer.AnswersService;
import pl.alekaug.quizplatform.question.exceptions.ClosedQuestionHavingNoAnswers;
import pl.alekaug.quizplatform.question.exceptions.OpenedQuestionHavingAnswers;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class QuestionsService {
    @Autowired
    private QuestionsRepository questionsRepository;
    @Autowired
    private AnswersService answersService;

    public Optional<Question> getById(long id) throws IllegalArgumentException {
        return questionsRepository.findById(id);
    }

    public List<Question> getAll() {
        return questionsRepository.findAll();
    }

    public Question add(Question question) throws IllegalArgumentException,
            ClosedQuestionHavingNoAnswers,
            OpenedQuestionHavingAnswers {
        /* First, add the question itself... */
        Question q = questionsRepository.save(question);
        /* Check its validity issues. */
        checkQuestionValidity(question);
        /* And add all its answers. */
        for (Answer answer : question.getAnswers())
            answersService.add(answer);
        return q;
    }

    public Question replace(long id, Question question) throws IllegalArgumentException,
            OpenedQuestionHavingAnswers,
            ClosedQuestionHavingNoAnswers {
        // TODO: Check links between changing question's answer objects
        Optional<Question> q = questionsRepository.findById(id);
        if (q.isEmpty())
            return null;
        Question original = q.get();
        original.changeTo(question);
        checkQuestionValidity(original);
        return questionsRepository.save(original);
    }

    public boolean remove(long questionId) throws IllegalArgumentException {
        if (!questionsRepository.existsById(questionId))
            return false;
        /* If a question is found by id, remove its answers first... */
        for (Answer answer : questionsRepository.getReferenceById(questionId).getAnswers())
            answersService.remove(answer.getId());
        /* Then delete the question itself. */
        questionsRepository.deleteById(questionId);
        return true;
    }

    public Integer removeAll() {
        /* First, delete all questions' dependent answers... */
        answersService.removeAll();
        /* Then remove all questions. */
        return questionsRepository.deleteAllReturnNumber();
    }

    private void checkQuestionValidity(Question question) throws ClosedQuestionHavingNoAnswers,
            OpenedQuestionHavingAnswers {
        /* If closed question does not contain answers, throw an appropriate exception. */
        if (Boolean.TRUE.equals(!question.getType()) && question.getAnswers().isEmpty())
            throw new ClosedQuestionHavingNoAnswers("No answers attached to closed-type question.");
        else if(Boolean.TRUE.equals(question.getType()) && !question.getAnswers().isEmpty())
            throw new OpenedQuestionHavingAnswers("No answers allowed in opened-type question.");
    }
}
