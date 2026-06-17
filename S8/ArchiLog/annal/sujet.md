Tout est adaptable dans le sujet ; Si un point ne vous convient pas, vous pouvez le changer ; mais il faut l'expliquer dans le readme !

Vous fournirez à la racine de votre projet un fichier README.md contenant une dizaine de lignes
- Votre nom
- BDD utilisée : (h2, postgres, sqlite, redis, mongo, ...)
- Création automatique des tables : (oui/non)
- Une ligne pour chaque question indiquant : (non fait / quasi fait / fait)
- Et éventuellement quelques explications si nécessaire en cas de "quasi fait"

## Objectif

L'objectif de ce projet consiste à réaliser une petite application de "sondage" permettant très rapidement de réaliser un sondage auprès des différents utilisateurs.

L'administrateur active une question, et les utilisateurs y répondent. In fine l'administrateur peut visualiser les résultats fournis pour cette question. (pour des raisons évidentes de simplicité on ne s'occupe pas de créer des questions ou des réponses, ni de créer des utilisateurs. Tout ceci se fera directement dans la base de données.)

Cette application possède donc 2 rôles distincts
- L'administrateur, qui peut activer une question et voir les résultats.
- L'utilisateur, qui ne peut que voter à la question proposée

Elle s'appuie sur 2 tables :
```
question(qno, libelle, active)
choix(cno, libchoix, statut, nbChoix, #qno)
```

La table question contient l'ensemble des questions préparées. A un instant donné, on n'active qu'une seule question pour le sondage. Les utilisateurs ne verront donc que cette question. Ce n'est pas un QCM mais uniquement un sondage sur une seule question. La table choix contient les réponses proposées pour cette question. Pour simplifier, les choix sont toujours exclusifs et gérés avec des RadioButtons. Toujours pour simplifier, pour chaque question une seule réponse est correcte (booléen statut à true). nbChoix correspond au nombre de personnes qui ont voté pour ce choix.

à titre d'exemple, un script SQL est fourni avec un exemple de création des tables et quelques données.

## Travail à réaliser

Q1. Créez, par la méthode de votre choix, un projet web Springboot, en version 3.5.0 dont le nom correspond à votre login :
`spring init -v 3.5.0 --build=maven -g=fr.but3 -d web,devtools,jpa,lombok,h2,postgres <monnom>`
N'oubliez pas de modifier le `.pom` et `application.properties` pour que l'application accepte les JSP (properties + tomcat-embed).

Q2. Faire en sorte qu'au lancement de l'application la trace SQL soit activée, les tables soient automatiquement créées (mode `create`) et remplies avec les données d'exemple et que l'ensemble des ordres SQL soient affichés lors du `mvn clean package`. (3 points)

Q3. Réalisez un endpoint `/activer` permettant d'afficher un formulaire avec la liste des questions de la base et qui permet d'activer une de ces questions en mode exclusif (radio). Une fois la question choisie on affichera un simple message récapitulatif (en `@ResponseBody`) disant "La question xxxxx vient d'être activée". (2 points)

Q4. Modifiez ce endpoint de manière à ce que seule la question sélectionnée soit activée et les autres désactivées, puis que tous les `nbChoix` correspondants soient réinitialisés à zéro." (3 points)

Q5. Réalisez un endpoint `/voter` qui affiche en titre la question courante et dans un formulaire la liste des choix possibles en mode exclusif (radio). La sélection d'un des choix incrémentera le compteur `nbChoix` correspondant dans la base. Une fois validé on affichera un simple message récapitulatif (en `@ResponseBody`) disant "Votre choix a bien été pris en compte". (2 points)

## Une fois arrivé ici vous avez la moyenne

Q6. Modifiez le endpoint `/activer` pour qu'il appelle le formulaire `/voter`. (2 points)

Q7. Réalisez un endpoint `/voir` qui permet d'afficher en titre la question et en liste à points les différents choix avec leurs nombre de réponses respectifs. Modifiez le endpoint `/voter` pour qu'il appelle la vue `/voir`. (2 points)

Q8. Ajoutez à votre pom le starter `SpringSecurity`. Mettre en place un système d'authentification avec 2 rôles : `admin` et `user`. On pourra utiliser la configuration par défaut et les tables par défaut offertes par Spring. On créera directement dans le code (en `inMemory`) quatre utilisateurs : un utilisateur de rôle `admin` nommé `admin/admin`. Trois utilisateurs de rôle `user` nommés `user1/user1`, `user2/user2`, `user3/user3`. On pourra conserver le formulaire de login/mdp classique de SpringSecurity et désactiver la sécurité CSRF : `.csrf(csrf -&gt; csrf.disable()` Les URL `/activer` et `/voir` ne seront accessibles qu'au rôle `admin`. Le endpoint `/voter` ne sera accessible qu'au rôle `user`. Les mots de passe seront chiffrés en `bcrypt`. (3 points)

Q9. Mettre en haut de toutes les pages précédemment créées le nom de la personne connectée. (3 points)</monnom>
