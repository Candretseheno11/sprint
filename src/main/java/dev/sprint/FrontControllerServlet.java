package main.java.dev.sprint;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import main.java.dev.sprint.annotation.AsJson;
import main.java.dev.sprint.annotation.Bind;
import main.java.dev.sprint.annotation.Role;
import main.java.dev.sprint.constant.HttpMethod;

@MultipartConfig(maxFileSize = 10 * 1024 * 1024, maxRequestSize = 11 * 1024 * 1024)
@SuppressWarnings("unchecked")
public class FrontControllerServlet extends HttpServlet {
    private String viewFormat;
    private Map<UrlInfo, UrlMapping> actions;

    @Override
    public void init() throws ServletException {

        String viewPrefix = this.getInitParameter("view-prefix");
        String viewSuffix = this.getInitParameter("view-suffix");
        viewFormat = viewPrefix + "%s" + viewSuffix;

        ServletContext context = this.getServletContext();
        actions = (Map<UrlInfo, UrlMapping>) context.getAttribute("actions");
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    public void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getRequestURI().substring(request.getContextPath().length());
        if (path.isEmpty() || "/".equals(path)) {
            path = "/";
        }
        if (path.length() > 1 && path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }

        UrlInfo urlInfo = new UrlInfo(HttpMethod.valueOf(request.getMethod()), path);

        UrlMapping mapping = actions.get(urlInfo);
        if (actions.containsKey(urlInfo)) {

            try {
                Method method = mapping.getMethod();
                if (!hasRequiredRole(request, method)) {
                
                                        response.sendError(HttpServletResponse.SC_FORBIDDEN,
                            "Accès refusé : rôle utilisateur insuffisant");
                  /*   response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.setContentType("text/html;charset=UTF-8");
                    PrintWriter out = response.getWriter();
                    out.println("<html>");
                    out.println("<head><title>Accès refusé</title></head>");
                    out.println("<body>");
                    out.println("<h1>Vous ne disposez pas de l'autorisation requise !</h1>");
                    out.println("</body>");
                    out.println("</html>");
                    */ return;
                }

                Object controller = mapping.getController().getDeclaredConstructor().newInstance();

                Map<String, String[]> requestParameters =
                        request.getContentType() != null && request.getContentType().startsWith("multipart/")
                                ? Map.of()
                                : request.getParameterMap();
                Parameter[] methodParameters = method.getParameters();

                Object[] mArgs = new Object[methodParameters.length];
                for (int i = 0; i < methodParameters.length; i++) {
                    String mpName = methodParameters[i].getName();
                    Class<?> mpType = methodParameters[i].getType();
                    if (mpType.equals(ServletContext.class)) {
                        mArgs[i] = getServletContext();
                    } else if (mpType.equals(HttpServletRequest.class)) {
                        mArgs[i] = request;
                    } else if (mpType.equals(HttpServletResponse.class)) {
                        mArgs[i] = response;
                    } else if (methodParameters[i].isAnnotationPresent(Bind.class)) {
                        mArgs[i] = Utils.bindObject(requestParameters, mpType);
                    } else {
                        String[] rpValues = requestParameters.get(mpName);
                        if (mpType.isArray()) {
                            int rpLength = rpValues == null ? 0 : rpValues.length;
                            Object objs = java.lang.reflect.Array.newInstance(mpType.getComponentType(), rpLength);
                            for (int j = 0; j < rpLength; j++) {
                                java.lang.reflect.Array.set(objs, j, Utils.parse(rpValues[j], mpType.getComponentType()));
                            }
                            mArgs[i] = objs;
                        } else {
                            mArgs[i] = Utils.parse(rpValues == null ? null : rpValues[0], mpType);
                        }
                    }
                }

                Object returnValue = method.invoke(controller, mArgs);

                if (method.isAnnotationPresent(AsJson.class)) {
                    AsJson asJson = method.getAnnotation(AsJson.class);
                    response.setContentType("application/json");
                    PrintWriter out = response.getWriter();
                    if (asJson.raw()) {
                        out.print(returnValue);
                    } else {
                        Gson gson = new Gson();
                        out.print(gson.toJson(returnValue));
                    }
                } else {
                    if (returnValue instanceof ModelAndView) {
                        ModelAndView mav = (ModelAndView) returnValue;

                        for (Map.Entry<String, Object> entry : mav.getModel().entrySet()) {
                            request.setAttribute(entry.getKey(), entry.getValue());
                        }

                        RequestDispatcher dispatcher = request.getRequestDispatcher(
                                String.format(viewFormat, mav.getView()));

                        dispatcher.forward(request, response);

                    } else if (returnValue instanceof String) {
                        response.setContentType("text/html;charset=UTF-8");
                        response.getWriter().print(returnValue);

                    } else if (returnValue != null) {
                        response.setContentType("text/plain;charset=UTF-8");
                        response.getWriter().print(returnValue);
                    }
                }
            } catch (Exception e) {
                throw new ServletException(e);
            }

        } else {
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<html>");
            out.println("<head><title>Sprint</title></head>");
            out.println("<body>");
            out.println("<h1>Route inconnue !</h1>");
            out.println("<p>");
            out.println("<strong>Actions connues :</strong>");
            out.println("<ul>");
            for (UrlInfo url : actions.keySet()) {
                UrlMapping routeMapping = actions.get(url);
                out.println("<li>" + url.getUrl() + " → "
                        + routeMapping.getController().getName() + "::"
                        + routeMapping.getMethod().getName() + "</li>");
            }
            out.println("</ul>");
            out.println("</p>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    private boolean hasRequiredRole(HttpServletRequest request, Method method) {
        Role methodRole = method.getAnnotation(Role.class);
        Role classRole = method.getDeclaringClass().getAnnotation(Role.class);
        Role requiredRole = methodRole != null ? methodRole : classRole;

        if (requiredRole == null) {
            return true;
        }

        Set<String> configuredRoles = Session.getConfiguredRoles(getServletContext());
        boolean roleConfigured = false;
        for (String role : requiredRole.value()) {

           /*  if(role==null || !configuredRoles.contains(role.trim().toUpperCase(java.util.Locale.ROOT))) {
                continue;
            } */
            if (role != null && configuredRoles.contains(role.trim().toUpperCase(java.util.Locale.ROOT))) {
                roleConfigured = true;
                break;
            }
        }
        if (!roleConfigured) {
            return false;
        }

        Session session = new Session(request);
        if (session.getUser() == null) {
            return false;
        }
/*         return session.hasAnyRole(requiredRole.value());
 */        return session.hasAnyRole(getServletContext(), requiredRole.value());
    }
}
    