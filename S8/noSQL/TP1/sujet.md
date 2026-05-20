Résumé

Ce TP aborde les requêtes sous MongoDB.

N'hésitez pas à consulter la documentation en ligne sur le site de MongoDB : https://docs.mongodb.com/v4.2/reference/ (car la version installée est la 4.2 de MongoDB).

Initialisation du TP

1. Récupérer le fichier restaurants.json sur le moodle du cours dans la partie Travaux Pratiques/TP1
2. Comme mentionné dans le wiki
   (http://wikiportal.polytech-lille.fr/mediawiki/index.php/Logiciels_Linux_en_salle_de_TP):
   — La location de l'installation mongo est dans /usr/localTP/mongo
   — Rajouter ce chemin à $PATH
export PATH=$PATH:/usr/localTP/mongo/bin
   Si vous ne voulez pas le refaire à chaque fois que vous réouvrez la console, ajouter la ligne précédente dans votre fichier .bashrc
   — Tester que vous retrouvez bien l'exécutable mongo
   which mongo
3. créer un répertoire spécifique pour ce TP et allez-y. Ce sera le dossier racine de votre TP.
4. créer un répertoire pour les bases
   mkdir -p data/db
5. lancer le deamon dans un shell
   mongod --dbpath data/db/ &amp;
6. A la fin de la séance, arrêter le deamon dans un shell
   mongod --dbpath data/db/ --shutdown
7. vérifier si le deamon est bien en ecoute sur le port 27017
   netstat -tpan | grep mongo
8. Importer la base de données dans MongoDB.
   mongoimport --db=tp --collection=restaurants --drop --file=restaurants.json
   La commande précédente ajoute le contenu du fichier restaurants.json à la collection restaurants dans la base tp en supprimant préalablement une collection déjà existante (--drop).
9. Accéder à la base de données de manière graphique :
   (a) Lancer robot, une interface graphique pour mongo db.
   /usr/localTP/robot3t/bin/robo3t
   (b) Créez une nouvelle connection en vérifiant que l'adresse est bien localhost et le port 27017.
   (c) Connectez-vous à cette nouvelle connection.
   (d) Allez dans la collection restaurant de la base de données tp que vous avez créée précédemment.
   (e) Familiarisez-vous un peu avec Robot 3T, en particulier, regardez les boutons en haut à gauche. L'exécution d'une requête se fera par Ctrl + Enter ou en cliquant sur la flèche verte.

I Recherches

Répondez aux requêtes suivantes :

I.1. Récupérer la liste complète des restaurants. \
I.2. Récupérer le nombre total de restaurants. \
I.3. Récupérer la liste complète des restaurants où vous n'affichez que le nom des restaurants et le type de cuisine. \
I.4. Récupérer la liste complète des restaurants en triant par quartier puis par nom. \
I.5. Récupérer la liste des restaurants de Brooklyn. (6086 documents) \
I.6. Récupérer la liste des restaurants offrant de la cuisine italienne dans Brooklyn. (192 documents) \
I.7. Récupérer la liste des restaurants offrant de la cuisine italienne sur la 5ème avenue (champ street du sous document address égal à '5 Avenue') à Brooklyn. (13 documents) \
I.8. Récupérer la liste des restaurants offrant de la cuisine italienne sur la 5ème avenue à Brooklyn qui possède pizza dans leur nom. (2 documents) \
I.9. Récupérer la liste des restaurants du Bronx qui ont au moins une valeur pour un score (champ score du sous document grades) strictement inférieure à 10. (1799 documents) \
I.10. Récupérer la liste des restaurants du Bronx qui n'ont que des valeurs de score strictement inférieures à 10 (on utilisera la fonction $not). (384 documents) \
I.11. Même question en affichant que le nom des restaurants et les grades. (384 documents) \
I.12. Récupérer la liste des restaurants ayant un grade C et un score inférieur à 30. Attention, il faut que ce soit pour le même grade. (1067 documents) \
I.13. Récupérer la liste des quartiers présents dans la base. (6 documents) \
I.14. Récupérer le nombre de quartiers présents dans la base. \
I.15. Récupérer la liste des valeurs possibles pour le champ grade du sous document grades. (6 documents) \
I.16. Récupérer la liste des types de cuisine où il y a au moins un restaurant qui a au moins 2 scores et où tous ses scores sont supérieurs à 20. (26 documents) \
I.17. Trouver les restaurants à moins de 1 kilomètre du point (-73.93414657, 40.82302903). Il faut utiliser les fonctions $geoWithin et $centerSphere. (171 documents) \
