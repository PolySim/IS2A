# Correction TP2 : Régression pénalisée

## Préambule

On note `y = brozek` la variable à prédire, et `X` l'ensemble des autres variables explicatives.

Pour obtenir des résultats reproductibles pour les validations croisées, on fixe une graine aléatoire.

```r
library(glmnet)

set.seed(123)
fat <- readRDS("fat.Rds")
str(fat)
summary(fat)
sum(is.na(fat))
```

Le jeu contient `252` individus et `16` variables, sans valeur manquante.

## 1. Analyse descriptive rapide

Code possible :

```r
dim(fat)
summary(fat)
apply(fat, 2, sd)
boxplot(fat, las = 2, cex.axis = 0.7)
```

Commentaires :

- `brozek` varie de `0` a `45.1`, pour une moyenne d'environ `18.94`.
- Les variables de circonference (`chest`, `abdom`, `hip`, `thigh`, etc.) presentent une dispersion moderee a forte.
- Il n'y a pas de donnees manquantes.
- Une observation semble atypique sur `height` (minimum `29.5`), ce qui merite un regard critique sur la qualite des donnees.

## 2. Matrice des correlations

```r
library(corrplot)

Cor <- cor(fat)
round(Cor, 3)

corrplot(Cor, method = "color", type = "upper", addCoef.col = "black",
         tl.col = "black", tl.srt = 45)
```

Les correlations les plus fortes avec `brozek` sont :

```r
sort(round(Cor[setdiff(rownames(Cor), "brozek"), "brozek"], 3), decreasing = TRUE)
```

Resultats principaux :

- `abdom` : `0.814`
- `adipos` : `0.728`
- `chest` : `0.703`
- `hip` : `0.626`
- `weight` : `0.613`

Commentaires :

- La graisse corporelle est tres fortement liee a la circonference abdominale.
- Plusieurs variables anthropometriques sont fortement correlees entre elles, ce qui suggere de la colinearite.
- Cette colinearite justifie l'interet des methodes penalisees comme Lasso, Ridge et Elastic Net.

## 3. Normalisation et regression lineaire multiple

```r
X <- scale(fat[, setdiff(names(fat), "brozek")])
fat_std <- data.frame(brozek = fat$brozek, X)

mod_lm <- lm(brozek ~ ., data = fat_std)
summary(mod_lm)
```

Resultat global :

- `R^2 = 0.9700`
- `R^2 ajuste = 0.9681`

Le modele lineaire sur toutes les variables ajuste tres bien les donnees, mais les coefficients sont interpretes dans un contexte de forte colinearite.

## 4. Lasso avec `lambda = 0.1`

```r
X <- scale(as.matrix(fat[, setdiff(names(fat), "brozek")]))
y <- fat$brozek

lasso_01 <- glmnet(X, y, alpha = 1, lambda = 0.1, standardize = FALSE)
coef(lasso_01)
```

Support obtenu pour `lambda = 0.1` :

- `weight`
- `height`
- `free`
- `chest`
- `abdom`
- `thigh`
- `knee`
- `ankle`
- `biceps`
- `forearm`

Le support est donc de cardinal `10`.

## 5. Chemin de regularisation du Lasso

```r
lasso_path <- glmnet(X, y, alpha = 1, standardize = FALSE)
plot(lasso_path, xvar = "lambda", label = TRUE)

nz <- apply(as.matrix(lasso_path$beta) != 0, 2, sum)
data.frame(lambda = lasso_path$lambda, support = nz)
```

Une valeur de `lambda` donnant un support de cardinal `5` est :

- `lambda ≈ 0.6736`

Support correspondant :

- `free`
- `abdom`
- `thigh`
- `biceps`
- `forearm`

## 6. Choix de `lambda` par validation croisee pour le Lasso

```r
foldid <- sample(rep(1:10, length.out = nrow(X)))
cv_lasso <- cv.glmnet(X, y, alpha = 1, standardize = FALSE, foldid = foldid)

plot(cv_lasso)
cv_lasso$lambda.min
min(cv_lasso$cvm)
```

Resultat :

- `lambda_min ≈ 0.1150`
- `MSE CV min ≈ 3.1937`

Le support associe est le meme que pour `lambda = 0.1` dans cette etude.

## 7. Comparaison des performances

On compare :

- un modele MCO sur toutes les variables ;
- un modele Lasso avec `lambda = lambda_min` ;
- un modele MCO sur le support selectionne par le Lasso.

Exemple de comparaison par validation croisee externe :

```r
cv_mse <- function(kind, data, foldid) {
  se <- c()

  for (k in sort(unique(foldid))) {
    tr <- foldid != k
    te <- foldid == k

    xtr <- scale(as.matrix(data[tr, setdiff(names(data), "brozek")]))
    ctr <- attr(xtr, "scaled:center")
    scl <- attr(xtr, "scaled:scale")
    xte <- scale(as.matrix(data[te, setdiff(names(data), "brozek")]), center = ctr, scale = scl)
    ytr <- data$brozek[tr]
    yte <- data$brozek[te]

    if (kind == "ols_all") {
      fit <- lm(brozek ~ ., data = data[tr, ])
      pred <- predict(fit, newdata = data[te, ])
    }

    if (kind == "lasso_opt") {
      cvfit <- cv.glmnet(xtr, ytr, alpha = 1, standardize = FALSE)
      pred <- as.numeric(predict(cvfit, newx = xte, s = "lambda.min"))
    }

    if (kind == "post_lasso") {
      cvfit <- cv.glmnet(xtr, ytr, alpha = 1, standardize = FALSE)
      fit_lasso <- glmnet(xtr, ytr, alpha = 1, lambda = cvfit$lambda.min, standardize = FALSE)
      supp <- setdiff(rownames(coef(fit_lasso))[as.vector(coef(fit_lasso) != 0)], "(Intercept)")
      df_tr <- data.frame(brozek = ytr, xtr[, supp, drop = FALSE])
      df_te <- data.frame(xte[, supp, drop = FALSE])
      fit <- lm(brozek ~ ., data = df_tr)
      pred <- predict(fit, newdata = df_te)
    }

    se <- c(se, (yte - pred)^2)
  }

  mean(se)
}

cv_mse("ols_all", fat, foldid)
cv_mse("lasso_opt", fat, foldid)
cv_mse("post_lasso", fat, foldid)
```

Valeurs obtenues :

- MCO complet : `9.1089`
- Lasso optimal : `6.8075`
- MCO post-Lasso : `9.2363`

Conclusion :

- Le Lasso optimal est le meilleur des trois en prediction.
- Le MCO complet ajuste tres bien en echantillon, mais generalise moins bien.
- Le MCO post-Lasso ne fait pas mieux ici que le Lasso penalise.

## 8. Ridge avec `lambda = 0.1`

```r
ridge_01 <- glmnet(X, y, alpha = 0, lambda = 0.1, standardize = FALSE)
coef(ridge_01)
```

Support obtenu : toutes les variables explicatives sont conservees.

Autrement dit, le support est de cardinal `15`.

## 9. Chemin de regularisation du Ridge

```r
ridge_path <- glmnet(X, y, alpha = 0, standardize = FALSE)
plot(ridge_path, xvar = "lambda", label = TRUE)
```

Commentaire :

- Pour le Ridge, les coefficients sont retrécis continument vers `0`, mais ne deviennent en general pas exactement nuls pour des valeurs finies de `lambda`.
- Donc la taille du support ne varie pratiquement pas : elle reste egale a `15` pour toutes les variables, hors extremites numeriques du chemin.

## 10. Choix de `lambda` par validation croisee pour le Ridge

```r
cv_ridge <- cv.glmnet(X, y, alpha = 0, standardize = FALSE, foldid = foldid)
plot(cv_ridge)
cv_ridge$lambda.min
min(cv_ridge$cvm)
```

Resultat :

- `lambda_min ≈ 0.6282`
- `MSE CV min ≈ 5.8515`

## 11. Differences entre Lasso et Ridge

Commentaires :

- Le Lasso annule certains coefficients et effectue donc une selection de variables.
- Le Ridge conserve toutes les variables, mais reduit leur amplitude.
- En presence de forte colinearite, le Ridge stabilise souvent mieux les coefficients, alors que le Lasso favorise l'interpretabilite.

## 12. Elastic Net avec `lambda = 0.1`

```r
enet_01 <- glmnet(X, y, alpha = 0.1, lambda = 0.1, standardize = FALSE)
enet_09 <- glmnet(X, y, alpha = 0.9, lambda = 0.1, standardize = FALSE)

coef(enet_01)
coef(enet_09)
```

Comparaison :

- avec `alpha = 0.1`, le modele est proche du Ridge ; toutes les variables restent dans le modele ;
- avec `alpha = 0.9`, le modele est proche du Lasso ; plusieurs coefficients deviennent nuls.

Dans nos calculs :

- `alpha = 0.1` : `15` coefficients non nuls ;
- `alpha = 0.9` : `10` coefficients non nuls.

Donc plus `alpha` est grand, plus le modele devient sparse.

## 13. Couple `(lambda, alpha)` optimal

On peut balayer plusieurs valeurs de `alpha` et utiliser `cv.glmnet` pour choisir le meilleur `lambda` pour chaque `alpha`.

```r
alphas <- seq(0, 1, by = 0.05)

res <- do.call(rbind, lapply(alphas, function(a) {
  cvfit <- cv.glmnet(X, y, alpha = a, standardize = FALSE, foldid = foldid)
  data.frame(alpha = a, lambda = cvfit$lambda.min, mse = min(cvfit$cvm))
}))

res[order(res$mse), ]
```

Sur cette grille, le meilleur couple obtenu est :

- `alpha = 1.00`
- `lambda ≈ 0.1150`
- `MSE ≈ 3.1937`

Autrement dit, ici l'Elastic Net optimal se confond avec le Lasso.

## Conclusion generale

- La variable `abdom` est de loin la plus informative pour expliquer `brozek`.
- Le MCO complet ajuste tres bien les donnees, mais souffre de colinearite.
- Le Ridge conserve toutes les variables et regularise les coefficients.
- Le Lasso selectionne un sous-ensemble interpretable et donne ici la meilleure performance predictive.
- L'Elastic Net ne fait pas mieux que le Lasso sur cette base, au moins sur la grille testee.

## Remarque

Les valeurs exactes de `lambda` et des MSE peuvent legerement varier selon les plis de validation croisee si l'on ne fixe pas `set.seed()`.
