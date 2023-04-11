package pl.alekaug.quizplatform.pages.management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.alekaug.quizplatform.question.Question;
import pl.alekaug.quizplatform.question.QuestionsRepository;

import java.util.List;

@Controller
public class QuestionsManagerController {
    @Autowired
    private QuestionsRepository questionsRepository;

    private static final String MANAGER_PATH = "/manager";
    @GetMapping(path = MANAGER_PATH)
    public String questionsManager(Model model) {
        List<Question> questions = questionsRepository.findAll();
        model.addAttribute("questions", questions);
        return "manager";
    }
}
