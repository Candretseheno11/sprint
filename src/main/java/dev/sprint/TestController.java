package main.java.dev.sprint;

import java.util.ArrayList;
import java.util.List;
import main.java.dev.sprint.annotation.AsJson;
import main.java.dev.sprint.annotation.Controller;
import main.java.dev.sprint.annotation.Url;
import main.java.dev.sprint.constant.HttpMethod;

@Controller
public class TestController {

    @Url(value = "/api/test", method = HttpMethod.GET)
    @AsJson
    public List<String> getVoitreJson() {
        List<String> voiture = new ArrayList<>();
        voiture.add("TESLA");
        voiture.add("BMW");
        voiture.add("KIA");
        return voiture;
    }

    @Url(value = "/api/voir", method = HttpMethod.GET)
    @AsJson
    public String voir() {
        return "Salut";
    }
}

