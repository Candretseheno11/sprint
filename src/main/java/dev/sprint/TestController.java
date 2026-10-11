package main.java.dev.sprint;

import java.util.ArrayList;
import java.util.List;
import main.java.dev.sprint.annotation.AsJson;
import main.java.dev.sprint.annotation.Bind;
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
    public ModelAndView getFormulaire() {
        return new ModelAndView("formulaire");
    }

    @Url(value = "/api/test", method = HttpMethod.POST)
    @AsJson 
    public List<String> enregistrer( @Bind  Inscription inscription) {
        List<String> info = new ArrayList<>();
        info.add(inscription.getNom());
        info.add(inscription.getEmail());

      //  ModelAndView modelAndView = new ModelAndView("bonjour");
      //  modelAndView.set("nom", inscription.getNom());
      //  modelAndView.set("email", inscription.getEmail());
        return info;
    }
}
