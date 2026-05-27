# TP4 : Modèles Linéaires Mixtes

Ce jeu de données étudie l'angle de rupture de gâteaux au chocolat préparés selon trois recettes différentes (A, B, C) et cuits à six températures (175°F à 225°F).

| Variable  | Type      | Description                                      | Niveaux/Unité |
| --------- | --------- | ------------------------------------------------ | ------------- |
| replicate | Facteur   | Numéro du réplicat.                              | 1 à 15        |
| recipe    | Facteur   | Recette utilisée (A, B, ou C).                   | A, B, C       |
| angle     | Numérique | Angle de rupture du gâteau (variable réponse).   | Degrés        |
| temp      | Numérique | Température de cuisson (valeur numérique en °F). | 175 à 225     |
| rest      | Numérique | Temps de repos de la pâte en minutes.            | 0 à 30        |

1- Chargez le jeu de données et faites-en une rapide analyse descriptive. \
2- Proposez des représentations graphique pour comprendre le lien entre la variable angle et les autres variables. \
3- Ajustez une régression linéaire (modèle avec uniquement des effets fixes) de l'angle sur la recette et la température. Quels sont les effets estimés pour les recettes et les températures ? Quels sont les problèmes de ce modèle ? \
4- Ajustez un modèle linéaire mixte de l'angle sur la recette et la température en ajoutant un intercept aléatoire. Que déduisez-vous de la valeur d'intercept aléatoire ici ? \
5- Utilisez la fonction ranef pour déterminer les valeurs d'intercept aléatoire prédite pour le premier individu. \
6- Quel serait l'angle prédit pour un gâteau produit avec la recette $A$ et cuit à une température de $182.5^{\circ}\mathrm{F}$ ? \
7- L'intercept aléatoire ajouté ici est-il intéressant pour le modèle ? Utilisez le test du rapport de vraisemblance et comparez ce modèle au précédent. \
8- Ajustez un modèle linéaire mixte de l'angle sur la recette et la température en ajoutant un intercept aléatoire et une pente aléatoire pour la température. \
9- Analysez les résidus du dernier modèle. Semblent-il normaux et indépendants ? \
10- Déterminez les IC des variances en utilisant le bootstrap à l'aide de la fonction bootMer. Qu'en déduisez-vous sur l'effet de vos variables ? \
11- Comment évoluent les prédictions en fonction de la température pour chaque recette ? Quelle recette semble la plus résistante à la rupture ? \
12- Ajustez le modèle qui vous semble adéquat pour répondre à la question : la recette à-t-elle une influence sur l'angle ? \
