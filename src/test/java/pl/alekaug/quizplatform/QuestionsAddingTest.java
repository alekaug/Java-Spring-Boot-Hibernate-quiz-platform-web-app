package pl.alekaug.quizplatform;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.alekaug.quizplatform.answer.Answer;
import pl.aaugustyniak.quizplatform.question.*;
import pl.alekaug.quizplatform.question.Question;
import pl.alekaug.quizplatform.question.QuestionsRepository;


import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.alekaug.quizplatform.constants.RestConstants.QUESTIONS_MANAGEMENT_API_PATH;

import java.io.*;

@SpringBootTest
@AutoConfigureMockMvc
class QuestionsAddingTest {
    private static final String CLOSED_QUESTIONS_PATH = "/closed-questions/";
    private static final String OPENED_QUESTIONS_PATH = "/opened-questions/";

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private QuestionsRepository questionsRepo;

    @Test
    void closedQuestionsSingleTest() throws Exception {
        Question expected = Question.builder()
                .content("What is the meaning of life?")
                .answers(Answer.builder()
                                .content("Doing what you are the best at.")
                                .correct(false)
                                .build(),
                        Answer.builder()
                                .content("Reaching for anything you want.")
                                .correct(false)
                                .build(),
                        Answer.builder()
                                .content("42")
                                .correct(true)
                                .build())
                .type(false)
                .build();

        MvcResult result = mockMvc.perform(put(QUESTIONS_MANAGEMENT_API_PATH)
                        .content(readResource(CLOSED_QUESTIONS_PATH + "single-closed1.json"))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        Long addedId = Long.getLong(result.getResponse().getContentAsString());
        Question added = questionsRepo.getReferenceById(addedId);
        assertEquals(expected, added);
    }

    @Test
    void closedQuestionsSeriesTest() throws Exception {
        // TODO: Implement, but add ability to add list of questions....
        /*
        Question expected = Question.builder()
                .
                .build();
        MvcResult result = mockMvc.perform(put(QUESTIONS_MANAGEMENT_API_PATH)
                        .content(readFile(new File(CLOSED_QUESTIONS_PATH + "series-closed1.json")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        Long addedId = Long.getLong(result.getResponse().getContentAsString());
        Question added = questionsRepository.getReferenceById(addedId);
        assertEquals(expected, added);
        */
    }

    private static String readResource(String file) throws Exception {
        try (InputStream input = QuestionsAddingTest.class.getResourceAsStream(file)) {
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
