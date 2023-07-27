package pl.alekaug.quizplatform.question;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.alekaug.quizplatform.question.exceptions.ClosedQuestionHavingNoAnswers;
import pl.alekaug.quizplatform.question.exceptions.ClosedQuestionHavingTooManyAnswersException;
import pl.alekaug.quizplatform.question.exceptions.OpenedQuestionHavingAnswers;

import static pl.alekaug.quizplatform.constants.RestConstants.*;

@RestController
@RequestMapping(QUESTIONS_MANAGEMENT_API_PATH)
public class QuestionsManagementController {
    private final Logger logger = LoggerFactory.getLogger(QuestionsManagementController.class);
    @Autowired
    private QuestionsService questionsService;

    @PutMapping(name = "Add new question.",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Long addQuestion(@RequestBody Question question) {
        final String receivedQuestion = question.toString();
        logger.debug(receivedQuestion);
        try {
            return questionsService.add(question).getId();
        } catch (OpenedQuestionHavingAnswers | ClosedQuestionHavingNoAnswers |
                 ClosedQuestionHavingTooManyAnswersException e) {
            logger.error(e.getMessage());
            logger.error(receivedQuestion);
            return -1L;
        }
    }

    @PatchMapping( name = "Modify existing question." )
    public Long modifyQuestion(@RequestParam(QUESTION_PATH_ID_PATH) long questionId,
                                   @RequestBody Question question) {
        final String receivedQuestion = question.toString();
        logger.debug(receivedQuestion);
        try {
            return questionsService.replace(questionId, question).getId();
        } catch (OpenedQuestionHavingAnswers | ClosedQuestionHavingTooManyAnswersException | ClosedQuestionHavingNoAnswers e) {
            logger.error(e.getMessage());
            final String errorMsg = "There was an issue with removing question of id %d.".formatted(questionId);
            logger.error(errorMsg);
            return -1L;
        }
    }

    @DeleteMapping( name = "Remove question." )
    public boolean removeQuestion(@RequestParam(QUESTION_PATH_ID_PATH) long questionId) {
        try {
            final boolean result = questionsService.remove(questionId);
            final String infoMsg = "Question with id equal %d removed successfully.".formatted(questionId);
            logger.info(infoMsg);
            return result;
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
            final String errorMsg = "There was an issue with removing question of id %d.".formatted(questionId);
            logger.error(errorMsg);
            return false;
        }
    }

    @DeleteMapping( name = "Remove all questions.", path = QUESTIONS_MANAGEMENT_API_PURGE_SUFFIX )
    public Integer removeAllQuestions() {
        return questionsService.removeAll();
    }
}
