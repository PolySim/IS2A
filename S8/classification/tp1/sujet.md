# Contents

1 Analyse préliminaire du jeu de données iris 1
2 Analyse factorielle discriminante 2
2.1 Calcul des matrices de variance-covariance 2
2.2 Réalisation de l'AFD 2
2.3 Calcul des scores discriminants 3

# 1 Analyse préliminaire du jeu de données iris

Dans cette partie on utilisera les données iris. Faire data("iris") dans R.

1. Renommer les variables Sepal.Length, Sepal.Width, Petal.Length, Petal.Width, Species en X1, X2, X3, X4, Y.
2. Représenter graphiquement le lien entre X1 et Y. Faire de même pour les autres variables.

On pourra utiliser : plot(X1-Y, data=iris), ggplot(aes(x = Y, y = X1, fill = Y), data = iris) + geom_boxplot() ou même

```r
library(ggplot2)
library(tidyr)
library(dplyr)
iris %&gt;%
gather("variable","mesure",-Y) %&gt;%
ggplot(aes(x = Y, y = mesure, col = Y)) +
geom_boxplot() +
facet_wrap(- variable, scales = "free_y")
```

3. En utilisant la fonction pairs avec l'option col, représenter graphiquement les liens entre couples de variables quantitatives et le groupe.

On pourra aussi utiliser la fonction ggpairs du package GGally

4. Réaliser l'ANOVA de X1 en fonction de Y et obtenir le $R^2$ associé, faire de même pour les autres variables. A partir des p-values, indiquer si la variable Y a une influence sur l'ensemble des variables. Quelle est la variable la mieux expliquée par Y ?

Afin d'automatiser les calculs, on pourra utiliser le code suivant :

```r
allanov&lt;-lapply(1:4,FUN=function(i){
anova(lm(get(paste0("X",i))-Y,data=iris))
})
allpval&lt;-sapply(allanov,FUN=function(x) x$~Pr(&gt;F)~[1])
#sapply équivaut ici à unlist(lapply())
```

```r
allpval
allr2&lt;-sapply(summary(lm(cbind(X1,X2,X3,X4)-Y,data=iris)), FUN=function(x) x$r.squared)
allr2
```

# 2 Analyse factorielle discriminante

## 2.1 Calcul des matrices de variance-covariance

1. Calculer $V$ la matrice de variance-covariance globale, à partir de la fonction cov.wt en utilisant l'option method = "ML". Expliquer à quoi sert cette option. Attention on prendra garde de récupérer le bon élément de sortie de la fonction cov.wt, fonction qui ressort une liste contenant entre autres cov, center, ...

**Remarque** : si vous voulez interpréter les dépendances 2 à 2, il faut calculer la matrice des corrélations, option cor = TRUE.

2. Calculer les matrices $W_i$, $i = 1, \ldots, 3$, de covariance intra-groupe, de la même façon que précédemment, mais en sélectionnant à chaque fois les individus nécessaires au calcul de la covariance.

En déduire $W$ à l'aide la formule :

$$
W = \frac{n_1 W_1 + n_2 W_2 + n_3 W_3}{n}
$$

**Remarque** : On peut utiliser la commande by pour automatiser le calcul des matrices de variance-covariance intra-groupe. Les effectifs par groupe s'obtiennent via la commande table.

3. Calcul de la matrice de variance-covariance inter-groupes $B$.

- Calculer les vecteurs des moyennes pour chaque groupe $\bar{X}_i$ (vous pourrez éventuellement vous aider de la fonction by). Puis constituez la matrice G de centres des classes constituée d'une colonne par variable et d'une ligne par sous-espèce.
- En déduire :

$$
B = \sum_{i=1}^{K} \frac{n_i}{n} (\bar{X}_i - \bar{X}) (\bar{X}_i - \bar{X})'
$$

où $\bar{X}_i$, $\bar{X}$ sont respectivement les vecteurs colonnes des moyennes intra-classes et de la moyenne globale. Pour cela on pourra remarquer que $B$ est la matrice de covariance des centres des classes pondérés par leurs effectifs (penser à cov.wt et à son argument wt).

4. Vérifier numériquement qu'on retrouve bien : $V = W + B$.

## 2.2 Réalisation de l'AFD

On rappelle que dans R l'ACP peut se réaliser à la main comme suit :

```r
ACP=eigen(V)$vectors
c=as.matrix(iris[,1:4])%*%ACP[,1:2]
plot(c,col=iris$Y)
```

1. Quel est le pourcentage d'inertie expliqué par chacun des axes ? Par les deux premiers axes ?

2. Adapter le code pour réaliser l'AFD, sachant qu'en AFD, on diagonalise la matrice $V^{-1}B$ (on rappelle que l'inverse s'obtient avec la fonction solve et le produit matriciel avec l'opérateur $\%*\%$). Calculer les coordonnées $d_1$ et $d_2$ des points projetés sur les deux premières composantes discriminantes.

3. Quelle est la part de variance de $d_1$ expliquée par la classe ? De $d_2$ ?

Polytech'Lille
ISI^A 2A4
Classification Supervisée
2025-2026
W. Heyse

4. Reprendre le code précédent en remplaçant $V^{-1}B$ par $W^{-1}B$. Démontrer que l'on a la relation : $\mu_j = \frac{\lambda_j}{1 - \lambda_j}$ avec $\lambda_j$ les valeurs propres de $V^{-1}B$ et $\mu_j$ celles de $W^{-1}B$.

5. Comparer les résultats obtenus à ceux obtenus en ACP.

## 2.3 Calcul des scores discriminants

1. Calculer les fonctions de score pour chacun des groupes, ces fonctions nous serviront ensuite à affecter chaque individu au groupe de plus grand score (équivalent à la minimisation de la distance de Mahalanobis).

On rappelle que le calcul des fonctions de score pour chaque groupe s'effectue comme suit :

$$
s_i(x) = \alpha_{i0} + \alpha_{i1}x_1 + \alpha_{i2}x_2 + \alpha_{i3}x_3 + \alpha_{i4}x_4
$$

avec $\alpha_{i0} = -\ddot{X}_i'W^{-1}\ddot{X}_i$ et

$$
\left( \begin{array}{c} \alpha_{i1} \\ \vdots \\ \alpha_{ip} \end{array} \right) = 2W^{-1}\ddot{X}_i
$$

Construire le tableau des coefficients:

|         | Setosa | Versicolor | Virginica |
| ------- | ------ | ---------- | --------- |
| α₀      | α₁₀    | α₂₀        | α₃₀       |
| X₁ : α₁ | α₁₁    | α₂₁        | α₃₁       |
| X₂ : α₂ | α₁₂    | α₂₂        | α₃₂       |
| X₃ : α₃ | α₁₃    | α₂₃        | α₃₃       |
| X₄ : α₄ | α₁₄    | α₂₄        | α₃₄       |

Rappel : Dans l'AFD, la notion de score est liée au calcul de la règle de décision. Une observation $x = (x_1, x_2, \ldots, x_p)$ sera affectée au groupe avec le score $s_i(x)$ maximal.\ Rappel :

$$
\hat{y} = \arg \min_i (x - \ddot{X}_i)'W^{-1}(x - \ddot{X}_i)
$$

Ce calcul revient à maximiser $2x'W^{-1}\ddot{X}_i - \ddot{X}_i'W^{-1}\ddot{X}_i$.

2. Calculer les scores des individus à partir de cette règle (simple calcul matriciel, on pourra rajouter une colonne de 1 à la matrice des données à l'aide de la fonction cbind).

3. En déduire le classement de chacun des individus à partir de ces scores (en utilisant de façon appropriée les fonctions apply et which.max).
