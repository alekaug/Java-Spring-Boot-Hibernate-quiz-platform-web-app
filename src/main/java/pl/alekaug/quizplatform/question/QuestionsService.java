package pl.alekaug.quizplatform.question;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pl.alekaug.quizplatform.answer.Answer;
import pl.alekaug.quizplatform.answer.AnswersService;
import pl.alekaug.quizplatform.question.exceptions.ClosedQuestionHavingNoAnswers;
import pl.alekaug.quizplatform.question.exceptions.ClosedQuestionHavingTooManyAnswersException;
import pl.alekaug.quizplatform.question.exceptions.OpenedQuestionHavingAnswers;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class QuestionsService {
    private final QuestionsRepository questionsRepository;
    private final AnswersService answersService;

    public QuestionsService(QuestionsRepository questionsRepository, AnswersService answersService) {
        this.questionsRepository = questionsRepository;
        this.answersService = answersService;
    }

    public Optional<Question> getById(long id) throws IllegalArgumentException {
        return questionsRepository.findById(id);
    }

    public List<Question> getAll() {
        final List<Question> questions = questionsRepository.findAll();
        questions.sort(Comparator.comparing(Question::getId));
        return questions;
    }


    public List<Question> getAllClosed() {
        return questionsRepository.getAllClosed();
    }

    public Question add(Question question) throws IllegalArgumentException,
            ClosedQuestionHavingNoAnswers,
            OpenedQuestionHavingAnswers,
            ClosedQuestionHavingTooManyAnswersException {
        /* First, add the question itself... */
        Question q = questionsRepository.save(question);
        /* Check its validity issues. */
        checkQuestionValidity(question);
        /* And add all its answers. */
        if (question.getAnswers() == null)
            return q;
        for (Answer answer : question.getAnswers())
            answersService.add(answer);
        return q;
    }

    public Question replace(long id, Question question) throws IllegalArgumentException,
            OpenedQuestionHavingAnswers,
            ClosedQuestionHavingNoAnswers,
            ClosedQuestionHavingTooManyAnswersException {
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
//        /* First, delete all questions' dependent answers... */
//        answersService.removeAll();
        /* Then remove all questions. */
        return questionsRepository.deleteAllReturnNumber();
    }

    private void checkQuestionValidity(Question question)
            throws ClosedQuestionHavingNoAnswers,
            OpenedQuestionHavingAnswers,
            ClosedQuestionHavingTooManyAnswersException {
        final Question.Type type = question.getType();
        final Collection<Answer> answers = question.getAnswers();
        boolean empty = false;
        if (answers == null || answers.size() == 0) empty = true;
        /* Opened question having any answers */
        if (type.equals(Question.Type.OPEN) && !empty)
            throw new OpenedQuestionHavingAnswers("No answers allowed in opened-type question.");
        /* Closed (single) question having less than 2 answers */
        else if (type.equals(Question.Type.SINGLE) && empty) {
            if (answers.size() < 2)
                throw new ClosedQuestionHavingNoAnswers("No sufficient amount of answers attached to closed-type question. (at least two for closed-single)");
            if (answers.stream().filter(Answer::isCorrect).count() > 1)
                throw new ClosedQuestionHavingTooManyAnswersException();
        }
        /* Opened question having any answers */
        else if (type.equals(Question.Type.MULTI) && empty)
            throw new ClosedQuestionHavingNoAnswers("No sufficient amount of answers attached to closed-type question. (at least one for closed-multi)");
    }
}
