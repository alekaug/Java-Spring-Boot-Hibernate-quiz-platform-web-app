package pl.alekaug.quizplatform;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.alekaug.quizplatform.answer.Answer;
import pl.alekaug.quizplatform.question.Question;
import pl.alekaug.quizplatform.question.QuestionsRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.alekaug.quizplatform.constants.RestConstants.*;
import static pl.alekaug.quizplatform.util.ResourceReader.readResource;

@SpringBootTest
@AutoConfigureMockMvc
public class QuestionsTests {
    /** Failure messages */
    private static final String EXCEPTION_EXPECTED_MESSAGE = "Exception was expected.";
    private static final String EXCEPTION_NOT_EXPECTED_MESSAGE = "Exception was not expected.";

    /** Paths */
    private static final String CLOSED_QUESTIONS_PATH = "/closed-questions/";
    private static final String OPENED_QUESTIONS_PATH = "/opened-questions/";

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private QuestionsRepository questionsRepo;

    @Disabled
    @Test
    void getQuestionWithoutPermission() {
        // TODO: Implement security layer; return 403 code
    }

    @Disabled
    @Test
    void getAllQuestionsWithoutPermission() {
        // TODO: Implement security layer; return 403 code
    }

    @Test
    void addClosedQuestionWithoutAnswers() {
        Question question = Question.builder()
                .content("Closed-type question without answers is not allowed.")
                .answer(null)
                .type(1)
                .build();
        ObjectMapper mapper = new ObjectMapper();
        assertThrows(ServletException.class, () -> {
            String questionJson = mapper.writeValueAsString(question);
            mockMvc.perform(put(QUESTIONS_MANAGEMENT_API_PATH)
                            .content(questionJson)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().is5xxServerError());
            fail(EXCEPTION_EXPECTED_MESSAGE);
        });
    }

    @Test
    void addOpenedQuestionWithAnswers() {
        Question question = Question.builder()
                .content("Opened-type question with answers is not allowed.")
                .answer(Answer.builder().content("I am the only answer.").correct(true).build())
                .build();
        ObjectMapper mapper = new ObjectMapper();
        assertThrows(ServletException.class, () -> {
            String questionJson = mapper.writeValueAsString(question);
            mockMvc.perform(put(QUESTIONS_MANAGEMENT_API_PATH)
                            .content(questionJson)
                            .accept(MediaType.APPLICATION_JSON)
                           .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().is5xxServerError());
            fail(EXCEPTION_EXPECTED_MESSAGE);
        });
    }

    @Test
    void getRemoveNotExistingQuestion() {
        final Long questionIdToBeDeleted = Long.MAX_VALUE;
        try {
            // Check first, if exists..
            MvcResult result = mockMvc.perform(
                    get(QUESTIONS_API_PATH + '/' + questionIdToBeDeleted))
                    .andExpect(status().isOk())
                    .andReturn();
            String resultMessage = result.getResponse().getContentAsString();
            assertEquals("null", resultMessage);

            // ..then remove.
            result = mockMvc.perform(delete(QUESTIONS_MANAGEMENT_API_PATH)
                            .queryParam(QUESTION_PATH_ID_PATH, String.valueOf(questionIdToBeDeleted)))
                    .andExpect(status().isOk())
                    .andReturn();
            resultMessage = result.getResponse().getContentAsString();
            assertEquals(false, Boolean.valueOf(resultMessage));
        } catch (Exception e) {
            fail(EXCEPTION_NOT_EXPECTED_MESSAGE);
        }
    }

    @Test
    void addSingleClosedQuestionTest() {
        try {
            mockMvc.perform(put(QUESTIONS_MANAGEMENT_API_PATH)
                            .content(readResource(CLOSED_QUESTIONS_PATH + "single-closed1.json"))
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();
        } catch (Exception e) {
            fail(EXCEPTION_NOT_EXPECTED_MESSAGE);
        }
    }

    @Test
    @Disabled
    void modifyNotExistingQuestion() {

    }

    /** Tests focused more on answers validity in closed-type questions. */

    @Test
    @Disabled
    void addAnswerWithoutContent() {
        // TODO: To do
    }

    @Test
    @Disabled
    void addAnswerWithoutCorrectness() {
        // TODO: To do
    }
}
