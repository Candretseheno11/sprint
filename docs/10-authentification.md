# Sprint 10 : Authentification et rôles

## Attribution des rôles

Le rôle n’est pas choisi ni envoyé par le formulaire de connexion. Il est associé à l’utilisateur dans `web.xml` :

```xml
<context-param>
    <param-name>user-roles</param-name>
    <param-value>andry=ADMIN;alice=USER</param-value>
</context-param>
```

Le nom saisi pendant la connexion devient l’identité de l’utilisateur dans la session :

```java
new Session(request).connect(nom);
```

Lors du contrôle d’une route, le framework prend le nom enregistré dans la session puis cherche son rôle dans la configuration `user-roles`. Le rôle n’est donc jamais stocké comme attribut séparé de la session.

Une route protégée utilise toujours l’annotation `@Role` :

```java
@Url(value = "/api/voiture", method = HttpMethod.GET)
@Role("ADMIN")
public List<String> getVoitureJson() {
    // ...
}
```

La liste `<roles>` reste optionnelle. Elle permet de déclarer les rôles connus par l’application :

```xml
<context-param>
    <param-name>roles</param-name>
    <param-value>USER;ADMIN</param-value>
</context-param>
```