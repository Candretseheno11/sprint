package main.java.dev.sprint;

import java.util.ArrayList;
import java.util.List;
import main.java.dev.sprint.annotation.AsJson;
import main.java.dev.sprint.annotation.Controller;
import main.java.dev.sprint.annotation.Url;
import main.java.dev.sprint.constant.HttpMethod;

@Controller
public class TestController {

    @Url(value = "/", method = HttpMethod.GET)
    public ModelAndView accueil() {
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.set("message", "Bienvenue dans Sprint.");
        return modelAndView;
    }

    @Url(value = "/api/voiture", method = HttpMethod.GET)
    @AsJson
    public List<String> getVoitureJson() {
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

    @Url(value = "/api/test", method = HttpMethod.GET)
    public String getFormulaire() {
        return """
                <!DOCTYPE html>
                <html lang="fr">
                <head><meta charset="UTF-8"><title>Formulaire</title></head>
                <body>
                    <h1>Inscription</h1>
                    <form action="/api/test" method="post">
                        <input type="text" name="nom" placeholder="Votre nom" required>
                        <input type="email" name="email" placeholder="Votre email" required>
                        <button type="submit">Envoyer</button>
                    </form>
                </body>
                </html>
                """;
    }
}
