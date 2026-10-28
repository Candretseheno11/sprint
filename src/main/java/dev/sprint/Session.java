package main.java.dev.sprint;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

import jakarta.servlet.ServletContext;
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

  public Set<String> getRoles(ServletContext context) {
        Set<String> result = new LinkedHashSet<>();
        String userName = getUser();
        if (userName == null || context == null) {
            return result;
        }

        String userRoles = context.getInitParameter("user-roles");
        if (userRoles == null || userRoles.isBlank()) {
            return result;
        }

        String normalizedUser = userName.trim().toLowerCase(Locale.ROOT);
        for (String mapping : userRoles.split("[;,]")) {
            if (mapping == null || !mapping.contains("=")) {
                continue;
            }
            String[] parts = mapping.split("=", 2);
            if (parts.length == 2
                    && parts[0].trim().toLowerCase(Locale.ROOT).equals(normalizedUser)) {
                String role = parts[1].trim().toUpperCase(Locale.ROOT);
                if (!role.isEmpty()) {
                    result.add(role);
                }
            }
        }
        return result;
    }

    public static Set<String> getConfiguredRoles(ServletContext context) {
        Set<String> roles = new LinkedHashSet<>();
        String configuredRoles = context == null ? null : context.getInitParameter("roles");
        if (configuredRoles == null || configuredRoles.isBlank()) {
            return roles;
        }

        for (String role : configuredRoles.split("[,;]")) {
            if (role != null && !role.isBlank()) {
                roles.add(role.trim().toUpperCase(Locale.ROOT));
            }
        }
        return roles;
    }

    public boolean hasRole(ServletContext context, String role) {
        return role != null && getRoles(context).contains(role.trim().toUpperCase(Locale.ROOT));
    }

    public boolean hasAnyRole(ServletContext context, String... requiredRoles) {
        if (requiredRoles == null || requiredRoles.length == 0) {
            return true;
        }
        Set<String> roles = getRoles(context);
        return Arrays.stream(requiredRoles)
                .filter(role -> role != null && !role.isBlank())
                .map(role -> role.trim().toUpperCase(Locale.ROOT))
                .anyMatch(roles::contains);
    }

    public void disconnect() {
        httpSession.invalidate();
    }
}
