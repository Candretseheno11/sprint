<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Envoi de fichier</title>
</head>
<body>
    <h1>Envoi de fichier</h1>
    <form action="${pageContext.request.contextPath}/upload" method="post" enctype="multipart/form-data">
        <label for="file">Fichier (tous types, 10 Mo maximum)</label>
        <input id="file" type="file" name="file" required>
        <button type="submit">Envoyer</button>
    </form>

    <% if (request.getAttribute("erreur") != null) { %>
        <p role="alert">${erreur}</p>
    <% } %>

    <% if (request.getAttribute("fichier") != null) { %>
        <h2>Fichier enregistré</h2>
        <dl>
            <dt>Nom d'origine</dt>
            <dd>${nomAffiche}</dd>
            <dt>Nom de stockage</dt>
            <dd>${fichier.storedName}</dd>
            <dt>Taille</dt>
            <dd>${fichier.size} octets</dd>
            <dt>Type déclaré</dt>
            <dd>${typeAffiche}</dd>
        </dl>
    <% } %>
</body>
</html>
