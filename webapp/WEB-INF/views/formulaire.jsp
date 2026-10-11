<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Inscription</title>
</head>
<body>
    <h1>Inscription</h1>
    <form action="${pageContext.request.contextPath}/api/test" method="post">
        <label for="nom">Nom</label>
        <input id="nom" type="text" name="nom" required>

        <label for="email">Email</label>
        <input id="email" type="email" name="email" required>

        <button type="submit">Envoyer</button>
    </form>
</body>
</html>