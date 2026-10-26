package main.java.dev.sprint;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
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

    @Url(value = "/upload", method = HttpMethod.GET)
    public ModelAndView afficherUpload() {
        return new ModelAndView("upload");
    }

    @Url(value = "/upload", method = HttpMethod.POST)
    public ModelAndView uploader(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        ModelAndView modelAndView = new ModelAndView("upload");
        Part part;
        try {
            part = request.getPart("file");
        } catch (IllegalStateException exception) {
            response.setStatus(HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE);
            modelAndView.set("erreur", "Le fichier dépasse la taille maximale de 10 Mo.");
            return modelAndView;
        }

        if (part == null || part.getSize() == 0) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            modelAndView.set("erreur", "Veuillez sélectionner un fichier non vide.");
            return modelAndView;
        }

        try {
            UploadedFile uploadedFile = Files.save(part);
            modelAndView.set("fichier", uploadedFile);
            modelAndView.set("nomAffiche", Utils.escapeHtml(uploadedFile.getOriginalName()));
            modelAndView.set("typeAffiche", Utils.escapeHtml(uploadedFile.getContentType()));
        } catch (IllegalArgumentException exception) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            modelAndView.set("erreur", exception.getMessage());
        }
        return modelAndView;
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

    @Url(value = "/session", method = HttpMethod.GET)
    public ModelAndView afficherSession(HttpServletRequest request) {
        return creerVueSession(request, null);
    }

    @Url(value = "/session/login", method = HttpMethod.POST)
    public ModelAndView connecter(String nom, HttpServletRequest request) {
        if (nom == null || nom.isBlank()) {
            return creerVueSession(request, "Veuillez saisir un nom.");
        }

        new Session(request).connect(nom);
        return creerVueSession(request, null);
    }

    @Url(value = "/session/logout", method = HttpMethod.POST)
    public ModelAndView deconnecter(HttpServletRequest request) {
        new Session(request).disconnect();
        return new ModelAndView("session");
    }

    private ModelAndView creerVueSession(HttpServletRequest request, String erreur) {
        ModelAndView modelAndView = new ModelAndView("session");
        String nom = new Session(request).getUser();
        modelAndView.set("connected", nom != null);
        modelAndView.set("nom", nom == null ? null : Utils.escapeHtml(nom));
        modelAndView.set("erreur", erreur);
        return modelAndView;
    }
}
