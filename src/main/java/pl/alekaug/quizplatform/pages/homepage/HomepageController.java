package pl.alekaug.quizplatform.pages.homepage;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.alekaug.quizplatform.CookieHelper;
import pl.alekaug.quizplatform.question.Question;
import pl.alekaug.quizplatform.question.QuestionsService;
import pl.alekaug.quizplatform.question.checker.*;
import pl.alekaug.quizplatform.question.checker.exceptions.*;

import java.util.List;

@Controller
public class HomepageController {
    private static final String HOMEPAGE_TEMPLATE = "index";
    private static final String RESULTS_TEMPLATE = "results";

    private final QuestionsService questionsService;
    private final AnswersCheckerService checkerService;

    public HomepageController(QuestionsService questionsService, AnswersCheckerService checkerService) {
        this.questionsService = questionsService;
        this.checkerService = checkerService;
    }

    @GetMapping("/")
    public String getHomepage(HttpServletResponse response, Model model, @CookieValue(name = "user", required = false) String user) {
        boolean dialogVisibility = user == null || user.isBlank();
        if (dialogVisibility) response.addCookie(CookieHelper.emptyCookie("user"));
        model.addAttribute("dialogOpen", dialogVisibility);
        model.addAttribute("questions", questionsService.getAll());
        return HOMEPAGE_TEMPLATE;
    }

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String checkAnswers(Model model, @RequestParam MultiValueMap<String, String> questionsForm) {
        final String cause = "cause";
        final String result = "result";
        float finalScore;

        try {
            finalScore = checkerService.checkAnswers(questionsForm);
        }  catch (QuestionNotFoundException e) {
            model.addAttribute(cause, "One of the questions couldn't be found.");
            return "error";
        } catch (QuestionsNotTheSameException e) {
            model.addAttribute(cause, "Questions set modified during score evaluation.");
            return "error";
        } catch (ScoreOutOfRangeException e) {
            model.addAttribute(cause, "Score evaluation issue (out of range).");
            return "error";
        } catch (DifferentAmountOfClosedQuestionsException e) {
            model.addAttribute(cause, "Question set modified during score evaluation.");
            return "error";
        } catch (Exception e) {
            model.addAttribute(cause, "Unknown.");
            return "error";
        }
        model.addAttribute(result, getResultString(finalScore));
        return RESULTS_TEMPLATE;
    }

    private static String getResultString(float score) {
        final String finalScore = String.format("%.0f%%", score * 100);
        if (score > .75f) return "Congratulations! You've got " + finalScore;
        else if (score > .5f) return "Not bad! You've got " + finalScore;
        else if (score > .25f) return "You've done quite well. You've got " + finalScore;
        else return "Maybe next time, it'll be better! You've got " + finalScore;
    }
}
