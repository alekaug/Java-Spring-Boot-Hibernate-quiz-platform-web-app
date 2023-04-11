package pl.alekaug.quizplatform.pages.homepage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.alekaug.quizplatform.question.Question;
import pl.alekaug.quizplatform.question.QuestionsRepository;

import java.util.Collections;
import java.util.List;

@Controller
public class QuizPageController {
    @Autowired
    QuestionsRepository questionsRepository;

    @GetMapping(path = "/quiz")
    public String quizPage(Model model) {
        List<Question> questions = questionsRepository.findAll();
        Collections.shuffle(questions);
        model.addAttribute("questions", questions);
        return "quiz";
    }
}
