package pl.alekaug.quizplatform.util;

import pl.alekaug.quizplatform.QuestionsTests;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public abstract class ResourceReader {
    public static String readResource(String path) throws Exception {
        try (InputStream input = QuestionsTests.class.getResourceAsStream(path)) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        }
    }
}
