package pl.alekaug.quizplatform.pages.homepage;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import pl.alekaug.quizplatform.question.Question;
import pl.alekaug.quizplatform.question.QuestionsService;
import pl.alekaug.quizplatform.question.checker.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Controller
public class HomepageController {
    private static final String HOMEPAGE_TEMPLATE = "index";
    private static final String RESULTS_TEMPLATE = "results";

    @Autowired
    private QuestionsService questionsService;
    @Autowired
    private AnswersCheckerService checkerService;
    @GetMapping("/")
    public String getHomepage(HttpServletResponse response, HttpServletRequest request, Model model) {
        List<Question> questions = questionsService.getAll();
        model.addAttribute("questions", questions);
        // Get session id cookie value (if exists)
//        Cookie cookie = getCookie(request.getCookies(), SESSION_ID_COOKIE);
//        if (cookie == null)
//            return HOMEPAGE_TEMPLATE;
//        long sessionId = Long.parseLong(cookie.getValue());
//
//        // Check if user exists and passed the quiz
//        Optional<Session> session = sessionsService.getById(sessionId);
//        if (session.isEmpty()) {
//            response.addCookie(emptyCookie(SESSION_ID_COOKIE));
//            return HOMEPAGE_TEMPLATE;
//        }
//
//        if (session.get().isPassed()) {
//            sessionsService.unregister(sessionId);
//            response.addCookie(emptyCookie(SESSION_ID_COOKIE));
//        }
//        else {
//            try {
//                response.sendRedirect("quiz");
//            } catch (IOException e) {
//                return HOMEPAGE_TEMPLATE;
//            }
//        }

        return HOMEPAGE_TEMPLATE;
    }

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String checkAnswers(Model model, @RequestParam MultiValueMap<String, String> questionsForm) {
        float finalScore = 0.f;

        try {
            finalScore = checkerService.checkAnswers(questionsForm);
        }  catch (QuestionNotFoundException e) {
            throw new RuntimeException(e);
        } catch (QuestionsNotTheSameException e) {
            throw new RuntimeException(e);
        } catch (ScoreOutOfRangeException e) {
            throw new RuntimeException(e);
        } catch (DifferentAmountOfClosedQuestionsException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            return "error";
        }
        model.addAttribute("score", String.format("%.0f", finalScore * 100));
        return RESULTS_TEMPLATE;
    }
}
