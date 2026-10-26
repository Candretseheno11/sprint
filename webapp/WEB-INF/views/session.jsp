<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Session utilisateur</title>
</head>
<body>
    <h1>Session utilisateur</h1>

    <% if (Boolean.TRUE.equals(request.getAttribute("connected"))) { %>
        <p>Utilisateur connecté : <strong>${nom}</strong></p>
        <form action="${pageContext.request.contextPath}/session/logout" method="post">
            <button type="submit">Se déconnecter</button>
        </form>
    <% } else { %>
       <% if (request.getAttribute("erreur") != null) { %>
            <p role="alert">${erreur}</p>
        <% } %>
        <form action="${pageContext.request.contextPath}/session/login" method="post">
            <label for="nom">Entrer votre nom :</label>
            <input id="nom" name="nom" type="text" required>
            <button type="submit">Se connecter</button>
        </form>
    <% } %>

    <p><a href="${pageContext.request.contextPath}/">Retour à l'accueil</a></p>
</body>
</html>
