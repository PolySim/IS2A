# TP2 : Régression Pénalisée

W. Heyse

On propose l'analyser le jeu de données fat disponible dans le fichier fat.Rds. Ce jeu de données contient des mesures anthropométriques et démographiques collectées sur 252 hommes. Il est couramment utilisé pour étudier les relations entre les circonférences corporelles, les indicateurs de masse grasse, et les métriques de densité corporelle. Les données ont été recueillies dans le cadre d'une étude sur la composition corporelle, où le pourcentage de graisse corporelle de chaque participant a été mesuré précisément par pesée hydrostatique. Cette méthode permet d'estimer la densité corporelle et, par extension, la proportion de graisse et de masse maigre.

| Variable | Description                                                                    | Unité  | Formule/Notes                                                   |
| -------- | ------------------------------------------------------------------------------ | ------ | --------------------------------------------------------------- |
| brozek   | Pourcentage de graisse corporelle (équation de Brozek)                         | %      | 457 / Density - 414.2                                           |
| age      | Âge                                                                            | années |                                                                 |
| weight   | Poids                                                                          | lbs    |                                                                 |
| height   | Taille                                                                         | inches |                                                                 |
| adipos   | Indice d'adiposité = Poids / Taille² (converti en kg/m²)                       | kg/m²  | Weight (lbs) / Height (inches)² (avec conversion si nécessaire) |
| free     | Poids sans graisse = (1 - fraction de graisse corporelle) \* Poids, via Brozek | lbs    | (1 - brozek/100) \* Weight                                      |
| neck     | Circonférence du cou                                                           | cm     |                                                                 |
| chest    | Circonférence de la poitrine                                                   | cm     |                                                                 |
| abdom    | Circonférence abdominale (au niveau du nombril et de la crête iliaque)         | cm     |                                                                 |
| hip      | Circonférence des hanches                                                      | cm     |                                                                 |
| thigh    | Circonférence de la cuisse                                                     | cm     |                                                                 |
| knee     | Circonférence du genou                                                         | cm     |                                                                 |
| ankle    | Circonférence de la cheville                                                   | cm     |                                                                 |
| biceps   | Circonférence du biceps (bras étendu)                                          | cm     |                                                                 |
| forearm  | Circonférence de l'avant-bras                                                  | cm     |                                                                 |
| wrist    | Circonférence du poignet (distal aux processus styloïdes)                      | cm     |                                                                 |

1- Chargez le jeu de données et faites-en une rapide analyse descriptive.
2- Calculez la matrice des corrélations de ce jeu de données, représentez et commentez-là.
3- Normalisez les données puis ajustez un modèle de régression linéaire sur les données permettant de prédire la variable brozek en fonction de toutes les autres variables.

1. $\lambda$ l’aide de la fonction glmnet, ajustez un modèle Lasso permettant de prédire la variable brozek en fonction de toutes les autres variables. Vous choisirez $\lambda =0.1$. Quel est le support de la regression ?
2. À l’aide de la fonction glmnet, représentez le chemin de régularisation en donnant pour lambda une séquence de valeurs de $\lambda$. Quelle valeur de $\lambda$ permet d’avoir un support de cardinal 5.
3. À l’aide de la fonction cv.glmnet, déterminez la valeur de $\lambda$ minimisant l’erreur de prédiction.
4. Comparez les performance d’un modèle de MCO estimé sur l’ensemble des variables, d’un modèle Lasso obtenu avec la valeur de $\lambda$ optimale et d’un modèle de MCO estimé sur les variables du support estimé par le Lasso et comparez les résultats.
5. À l’aide de la fonction glmnet, ajustez un modèle de régression Ridge permettant de prédire la variable brozek en fonction de toutes les autres variables. Vous choisirez $\lambda =0.1$. Quel est le support de la regression ?
6. À l’aide de la fonction glmnet, représentez le chemin de régularisation en donnant pour lambda une séquence de valeurs de $\lambda$. La taille du support varie-t-elle avec la valeur de $\lambda$ ?
7. À l’aide de la fonction cv.glmnet, déterminez la valeur de $\lambda$ minimisant l’erreur de prédiction.
8. Quelles différences sur les valeurs estimées notez vous ?
9. Ajustez mantenant un modèle d’Elasticnet avec $\lambda =0.1$ et $\alpha =0.1$ puis $\alpha =0.9$. Quelles sont les différences entre les deux modèles ?
10. Déterminez le couple $(\lambda ,\alpha )$ optimal et donnez le MSE associé à ce couple.
