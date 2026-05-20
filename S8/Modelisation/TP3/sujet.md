1- Télécharger la librairie `survival`, charger les données `lung` à l'aide de la commande `data(lung)` et proposer une analyse descriptive de ces données.

2- À l'aide de la fonction `swimmer_plot` du package `swimplot`, tracer le graphe de suivi des individus. Vous pourrez utiliser la fonction `swimmer_points` pour tracer le type d'événement. Que remarquez-vous sur ce graphe (en terme d'événements), qu'en déduisez-vous des événements ?

3- Quel est le temps moyen avant décès chez les femmes ?

4- Estimez la courbe de Kaplan-Meier pour l'ensemble des patients à l'aide des fonctions `Surv` et `survfit`. Que déduisez-vous de l'allure de cette courbe ?

5- Estimez la courbe de Kaplan-Meier en différenciant les patients selon leur sexe. Que déduisez-vous de l'effet du sexe sur la survie des individus ?

6- Ajuster un modèle de Cox simple avec age et sex comme covariables avec la fonction `coxph`. Interpréter les coefficients (hazard ratios, IC à 95 %, p-values).

7- Ajouter la variable `ph.ecog` au modèle précédent et omparer les modèles avec un test du rapport de vraisemblance, en utilisant la fonction `anova`. Qu'en déduisez-vous de l'ajout de la variable `ph.ecog` ?

8- Prédire le risque pour un patient de 60 ans, de sexe masculin, avec `ph.ecog = 1`.

9- Vérifier l'hypothèse de proportionnalité des risques avec un test de Schoenfeld à l'aide de la commande `cox.zph`.

10- Supposons que l'hypothèse soit violée pour la variable `age`, ajuster un modèle de cox stratifié.

11- Reprendre le modèle de la question 5 et déterminer le C-index du modèle.

12- Estimer par cross-validation cette concordance. Vous pourrez utiliser la fonction `Cindex` du package `glmnet`.
