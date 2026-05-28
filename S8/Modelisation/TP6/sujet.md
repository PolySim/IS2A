# TP6 : Régression PLS

1- Lancez les lignes de code ci-dessous.

```r
install.packages(c("pls", "caret", "ggplot2", "corrplot"))
library(pls)
library(caret)
library(ggplot2)
library(corrplot)
```

2- Chargez le jeu de données gasoline avec la commande data(gasoline). Décrivez le jeu de données et réalisez une analyse descriptive des données, concentrez-vous particulièrement sur les corrélation.

3- Ajustez un modèle de régression linéaire sur ce jeu de données ? Commentez les résultats obtenus.

4- Réalisez une ACP sur les variables explicatives.

5- Analysez votre ACP et déterminez le nombre d'axe utiles à retenir.

6- Ajustez un modèle de PCR sur les données à l'aide de la fonction pcr.

7- Explorez ce modèle : quels sont les coefficients associés aux variables, quel est l'impact du nombre de composantes sur les indicateurs de qualité du modèle, quel est le nombre de composantes optimal pour le problème de prédiction.

8- Déterminez le MSE de ce modèle.

9- Resprésentez et analysez les résidus de ce modèle.

10- Ajustez un modèle de PLS à l'aide de la fonction plsr.

11- Explorez ce modèle : quels sont les coefficients associés aux variables, quel est l'impact du nombre de composantes sur les indicateurs de qualité du modèle, quel est le nombre de composantes optimal pour le problème de prédiction.

12- Déterminez le MSE de ce modèle.

13- Resprésentez et analysez les résidus de ce modèle.

14- Comparez les résultats obtenus entre les deux modèles, lequel semble le plus adapté à votre problème ?

15- Quelle autre méthode auriez-vous pu appliquer ? Quelle différence cela aurait-il eu sur les résultats ?
