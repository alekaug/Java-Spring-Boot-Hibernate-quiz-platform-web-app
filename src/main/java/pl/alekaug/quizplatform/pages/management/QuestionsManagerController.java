package pl.alekaug.quizplatform.pages.management;

import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import pl.alekaug.quizplatform.question.Question;
import pl.alekaug.quizplatform.question.QuestionsManagementController;
import pl.alekaug.quizplatform.question.QuestionsRepository;

import java.util.List;

@Controller
public class QuestionsManagerController {
    @Autowired
    private QuestionsRepository questionsRepository;

    @Autowired
    private QuestionsManagementController questionsManagementController;

    private static final String MANAGER_PATH = "/manager";
    private static final String CREATE_QUESTION_PATH = "/manager/create-question";
    @GetMapping(path = MANAGER_PATH)
    public String questionsManager(Model model) {
        List<Question> questions = questionsRepository.findAll();
        model.addAttribute("questions", questions);
        return "manager";
    }

    @PostMapping(path = CREATE_QUESTION_PATH)
    public String createQuestion(@NotNull Question question) {
        questionsManagementController.addQuestion(question);
        return MANAGER_PATH;
    }
}
