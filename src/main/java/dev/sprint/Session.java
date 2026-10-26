package main.java.dev.sprint;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class Session {

    private static final String USER_ATTRIBUTE = Session.class.getName() + ".user";

    private final HttpServletRequest request;
    private final HttpSession httpSession;

    public Session(HttpServletRequest request) {
        this.request = request;
        this.httpSession = request.getSession();
    }

    public String getUser() {
        Object user = httpSession.getAttribute(USER_ATTRIBUTE);
        return user instanceof String ? (String) user : null;
    }

    public void connect(String userName) {
        if (userName == null || userName.isBlank()) {
            throw new IllegalArgumentException("Le nom ne peut pas être vide.");
        }

        request.changeSessionId();
        httpSession.setAttribute(USER_ATTRIBUTE, userName.trim());
    }

    public void disconnect() {
        httpSession.invalidate();
    }
}
