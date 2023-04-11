package pl.alekaug.quizplatform.question;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.alekaug.quizplatform.constants.RestConstants;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(RestConstants.QUESTIONS_API_PATH)
public class QuestionsController {
    private final Logger logger = LoggerFactory.getLogger(QuestionsController.class);
    @Autowired
    private QuestionsService questionsService;

    public QuestionsController() { /* NoOp */ }

    @GetMapping( name = "Get question by id.", path = "/{" + RestConstants.QUESTION_PATH_ID_PATH + "}" )
    public Optional<Question> getById(@PathVariable(RestConstants.QUESTION_PATH_ID_PATH) int questionId) {
        try {
            final String debugMsg = "Trying to get a question by id = %d.".formatted(questionId);
            logger.debug(debugMsg);
            return questionsService.getById(questionId);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
            return Optional.empty();
        }
    }

    @GetMapping( name = "Get all questions.", produces = MediaType.APPLICATION_JSON_VALUE )
    public List<Question> getAll() {
        return questionsService.getAll();
    }
}
