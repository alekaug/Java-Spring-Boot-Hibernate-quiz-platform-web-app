package pl.alekaug.quizplatform.pages.homepage;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

@Controller
public class HomepageController {
    private static final String NAME_ATTRIBUTE_NAME = "name";

    @GetMapping("/")
    public String getHomepage(@RequestParam(required = false) String name, Model model) {
        model.addAttribute(NAME_ATTRIBUTE_NAME, Objects.requireNonNullElse(name, "Stranger"));
        return "index";
    }
}
