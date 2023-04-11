package pl.alekaug.quizplatform;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HTTPStatusTest {

    @Value(value = "${local.server.port}")
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void checkWebApplicationStatus() {
        int responseCode = testRestTemplate.getForEntity("http://localhost:%s/".formatted(port), String.class)
                .getStatusCode().value();

        /* Expecting other code than 404 – not found */
        assertFalse(String.valueOf(responseCode).equals("404"));
    }
}
