package main.java.dev.sprint;

import main.java.dev.sprint.annotation.Controller;
import main.java.dev.sprint.annotation.Url;
import main.java.dev.sprint.constant.HttpMethod;

@Controller
public class TestController {

    @Url(value = "/", method = HttpMethod.GET)
    public void index() {
        // rien ici : la servlet affiche directement la page HTML
    }
}
