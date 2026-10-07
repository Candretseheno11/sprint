package main.java.dev.sprint;

import main.java.dev.sprint.annotation.Controller;
import main.java.dev.sprint.annotation.Url;
import main.java.dev.sprint.constant.HttpMethod;

@Controller
public class TestController {

    @Url(value = "/", method = HttpMethod.GET)
    public ModelAndView index() {
        ModelAndView mav = new ModelAndView("index");
        mav.set("message", "Bienvenue sur Sprint");
        return mav;
    }

    @Url(value = "/bonjour", method = HttpMethod.GET)
    public ModelAndView bonjour() {
        ModelAndView mav = new ModelAndView("bonjour");
        mav.set("message", "Salut depuis Sprint !");
        return mav;
    }
}
