package pl.alekaug.quizplatform;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.alekaug.quizplatform.question.QuestionsRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.alekaug.quizplatform.constants.RestConstants.QUESTIONS_MANAGEMENT_API_PATH;

import java.io.*;

@SpringBootTest
@AutoConfigureMockMvc
class QuestionsTest {
    private static final String CLOSED_QUESTIONS_PATH = "/closed-questions/";
    private static final String OPENED_QUESTIONS_PATH = "/opened-questions/";

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private QuestionsRepository questionsRepo;

    @Disabled
    @Test
    void getQuestionWithoutPermission() {
        // TODO: Implement security layer
    }

    @Disabled
    @Test
    void getAllQuestionsWithoutPermission() {
        // TODO: Implement security layer
    }

    @Test
    void addSingleClosedQuestionTest() throws Exception {
        mockMvc.perform(put(QUESTIONS_MANAGEMENT_API_PATH)
                        .content(readResource(CLOSED_QUESTIONS_PATH + "single-closed1.json"))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    private static String readResource(String file) throws Exception {
        try (InputStream input = QuestionsTest.class.getResourceAsStream(file)) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        }
    }

    private static JSONArray parseJsonArray(String content) throws JSONException {
        return new JSONArray(content);
    }
}
