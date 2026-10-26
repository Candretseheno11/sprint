# Upload de fichiers

La page de démonstration est accessible sur `/upload` depuis la page d’accueil. Elle accepte tous les types de fichiers et stocke chaque fichier sous un nom aléatoire, indépendamment de son nom d’origine. La taille maximale est de 10 Mo par fichier.

La classe `Files` applique les règles d’upload et enregistre le contenu dans le dossier `uploads` du répertoire de travail du serveur. Pour utiliser un autre emplacement (par exemple un volume persistant ou un stockage partagé entre plusieurs serveurs), configurez la propriété système `sprint.upload.dir` avec un chemin absolu. Le dossier de stockage doit rester hors du répertoire public de l’application.

`UploadedFile` contient les métadonnées indépendantes du stockage : nom d’origine, nom généré, taille et type MIME déclaré par le client. Ces métadonnées sont retournées à la page de démonstration ; leur persistance en base de données ou dans un stockage partagé reste à mettre en place selon l’infrastructure cible.

Le type MIME est fourni par le client et n’est pas une preuve du contenu réel. Comme tous les types sont acceptés pour cette démonstration, les fichiers sont stockés hors du répertoire web et ne sont pas servis directement par l’application.
