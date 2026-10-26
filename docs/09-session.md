Implementation de de la fonctionnalite session, tu creer une class Session qui prendre la session de l'utilisateur connecte ensuite lors et ensuite le tester via login ou juste un petit form

## Démonstration

La classe `Session` encapsule la session HTTP et permet de lire le nom de l'utilisateur connecté, de le connecter et de le déconnecter. La démonstration est accessible depuis l'accueil ou directement sur `/session` :

- Le formulaire POST `/session/login` stocke le nom dans la session HTTP.
- La page `/session` affiche l'utilisateur lors des requêtes suivantes.
- Le formulaire POST `/session/logout` invalide la session.

Cette connexion par nom sert uniquement à tester la persistance d'une session. Elle ne vérifie pas l'identité de l'utilisateur et ne remplace pas un vrai mécanisme d'authentification.