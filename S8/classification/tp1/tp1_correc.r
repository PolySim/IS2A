# =====================================================
# TP1 : Analyse Factorielle Discriminante - Jeu iris
# =====================================================

# Chargement des librairies nécessaires
library(ggplot2)
library(tidyr)
library(dplyr)
library(GGally)

# =====================================================
# 1. Analyse préliminaire du jeu de données iris
# =====================================================

cat("\n========================================\n")
cat("PARTIE 1 : ANALYSE PRELIMINAIRE\n")
cat("========================================\n\n")

# Chargement des données
data("iris")

# 1.1 Renommer les variables
cat("1.1 Renommage des variables\n")
iris <- iris %>%
  rename(
    X1 = Sepal.Length,
    X2 = Sepal.Width,
    X3 = Petal.Length,
    X4 = Petal.Width,
    Y = Species
  )

print(head(iris))

# 1.2 Représentations graphiques des liens entre variables et Y
cat("\n1.2 Représentations graphiques\n")
cat("Graphique boxplot par variable...\n")

p <- iris %>%
  gather("variable", "mesure", -Y) %>%
  ggplot(aes(x = Y, y = mesure, col = Y)) +
  geom_boxplot() +
  facet_wrap(~variable, scales = "free_y") +
  ggtitle("Distribution des variables par espèce")

print(p)

# 1.3 Matrice de dispersion avec couleurs
cat("\n1.3 Matrice de dispersion (pairs)\n")
colors <- c("setosa" = "red", "versicolor" = "green", "virginica" = "blue")
pairs(iris[, 1:4],
  col = colors[iris$Y], pch = 19,
  main = "Matrice de dispersion par espèce"
)
legend("topright", legend = levels(iris$Y), col = colors, pch = 19)

# Avec GGally
cat("\nMatrice de dispersion avec ggpairs...\n")
print(ggpairs(iris,
  columns = 1:4, aes(color = Y),
  title = "Matrice de dispersion avec ggpairs"
))

# 1.4 ANOVA et R²
cat("\n1.4 ANOVA et R²\n")

# Calcul automatique des ANOVAs
allanov <- lapply(1:4, FUN = function(i) {
  anova(lm(get(paste0("X", i)) ~ Y, data = iris))
})

allpval <- sapply(allanov, FUN = function(x) x$`Pr(>F)`[1])
cat("\nP-values des ANOVAs:\n")
print(allpval)

# Calcul des R²
allr2 <- sapply(summary(lm(cbind(X1, X2, X3, X4) ~ Y, data = iris)),
  FUN = function(x) x$r.squared
)
cat("\nR² pour chaque variable:\n")
print(allr2)

# Variable la mieux expliquée par Y
best_var <- names(allr2)[which.max(allr2)]
cat("\nVariable la mieux expliquée par Y:", best_var, "(R² =", max(allr2), ")\n")

# =====================================================
# 2. Analyse Factorielle Discriminante
# =====================================================

cat("\n========================================\n")
cat("PARTIE 2 : ANALYSE FACTORIELLE DISCRIMINANTE\n")
cat("========================================\n\n")

# 2.1 Calcul des matrices de variance-covariance

cat("2.1 Calcul des matrices de variance-covariance\n\n")

# Matrice de covariance globale V (avec method = "ML")
# method = "ML" donne le Maximum de Vraisemblance (divise par n, pas n-1)
cat("L'option method = 'ML' permet de calculer la matrice de covariance en utilisant\n")
cat("l'estimateur du Maximum de Vraisemblance, qui divise par n (et non n-1 comme\n")
cat("l'estimateur sans biais par défaut). Cela donne la vraie variance de l'échantillon.\n\n")
V_result <- cov.wt(iris[, 1:4], method = "ML")
V <- V_result$cov
cat("V - Matrice de covariance globale:\n")
print(V)

# Effectifs par groupe
n_i <- table(iris$Y)
n <- nrow(iris)
cat("\nEffectifs par groupe:\n")
print(n_i)

# Matrices de covariance intra-groupe Wi
W_list <- by(iris[, 1:4], iris$Y, function(x) cov.wt(x, method = "ML")$cov)
cat("\nMatrices de covariance intra-groupe:\n")
for (i in seq_along(W_list)) {
  cat("\nW", i, "(", names(W_list)[i], "):\n", sep = "")
  print(W_list[[i]])
}

# Matrice de covariance intra-groupe W pondérée
W <- (n_i[1] * W_list[[1]] + n_i[2] * W_list[[2]] + n_i[3] * W_list[[3]]) / n
cat("\nW - Matrice de covariance intra-groupe (pondérée):\n")
print(W)

# Matrice de covariance inter-groupe B
# Calcul des centres des classes
G <- by(iris[, 1:4], iris$Y, colMeans)
G_matrix <- do.call(rbind, G)
cat("\nCentres des classes (G):\n")
print(G_matrix)

# Moyenne globale
X_bar <- colMeans(iris[, 1:4])
cat("\nMoyenne globale:\n")
print(X_bar)

# Calcul de B avec cov.wt pondérée
B <- cov.wt(G_matrix, wt = as.numeric(n_i), method = "ML")$cov
cat("\nB - Matrice de covariance inter-groupe:\n")
print(B)

# Vérification V = W + B
cat("\nVérification V = W + B:\n")
cat("V:\n")
print(V)
cat("\nW + B:\n")
print(W + B)
cat("\nDifférence max (devrait être proche de 0):", max(abs(V - (W + B))), "\n")

# 2.2 Réalisation de l'AFD

cat("\n\n2.2 Réalisation de l'AFD\n\n")

# ACP pour comparaison
cat("--- Comparaison avec ACP ---\n")
ACP <- eigen(V)$vectors
c_acp <- as.matrix(iris[, 1:4]) %*% ACP[, 1:2]

# Pourcentage d'inertie expliquée en ACP
val_propres_acp <- eigen(V)$values
pct_inertie_acp <- val_propres_acp / sum(val_propres_acp) * 100
cat("Pourcentage d'inertie expliquée par axe (ACP):\n")
cat("Axe 1:", pct_inertie_acp[1], "%\n")
cat("Axe 2:", pct_inertie_acp[2], "%\n")
cat("Deux premiers axes:", sum(pct_inertie_acp[1:2]), "%\n")

# Plot ACP
plot(c_acp,
  col = iris$Y, pch = 19, main = "ACP - Projection sur les 2 premiers axes",
  xlab = "Composante 1", ylab = "Composante 2"
)
legend("topright", legend = levels(iris$Y), col = 1:3, pch = 19)

# AFD avec V^(-1)B
cat("\n--- AFD avec V^(-1)B ---\n")
V_inv_B <- solve(V) %*% B
afd_vb <- eigen(V_inv_B)

# Valeurs propres
lambda <- Re(afd_vb$values[1:4])
cat("Valeurs propres de V^(-1)B:\n")
print(lambda)

# Vecteurs propres
vep_vb <- Re(afd_vb$vectors[, 1:2])

# Coordonnées des points projetés
c_vb <- as.matrix(iris[, 1:4]) %*% vep_vb
colnames(c_vb) <- c("d1", "d2")

cat("\nCoordonnées des points projetés (premières lignes):\n")
print(head(c_vb))

# Plot AFD (V^(-1)B)
plot(c_vb,
  col = iris$Y, pch = 19, main = "AFD (V^(-1)B) - Projection sur les 2 premiers axes",
  xlab = "Discriminant 1", ylab = "Discriminant 2"
)
legend("topright", legend = levels(iris$Y), col = 1:3, pch = 19)

# Variance expliquée par la classe pour d1 et d2
r2_d1 <- summary(lm(c_vb[, 1] ~ iris$Y))$r.squared
cat("\nPart de variance de d1 expliquée par la classe:", r2_d1 * 100, "%\n")

r2_d2 <- summary(lm(c_vb[, 2] ~ iris$Y))$r.squared
cat("Part de variance de d2 expliquée par la classe:", r2_d2 * 100, "%\n")

# AFD avec W^(-1)B
cat("\n--- AFD avec W^(-1)B ---\n")
W_inv_B <- solve(W) %*% B
afd_wb <- eigen(W_inv_B)

# Valeurs propres
mu <- Re(afd_wb$values[1:4])
cat("Valeurs propres de W^(-1)B (mu):\n")
print(mu)

# Démonstration de la relation mu = lambda / (1 - lambda) :
# On a V = W + B, donc V^(-1)B a pour valeur propre lambda_j avec vecteur propre u_j :
#   V^(-1) B u_j = lambda_j u_j
#   => B u_j = lambda_j V u_j = lambda_j (W + B) u_j
#   => B u_j - lambda_j B u_j = lambda_j W u_j
#   => (1 - lambda_j) B u_j = lambda_j W u_j
#   => W^(-1) B u_j = lambda_j / (1 - lambda_j) u_j
# Donc mu_j = lambda_j / (1 - lambda_j), avec les mêmes vecteurs propres.

cat("\nVérification de la relation mu = lambda / (1 - lambda):\n")
mu_calc <- lambda / (1 - lambda)
cat("mu calculé:", mu_calc, "\n")
cat("mu obtenu:", mu[1:4], "\n")
cat("Différence:", max(abs(mu[1:4] - mu_calc)), "\n")

# Vecteurs propres et coordonnées
vep_wb <- Re(afd_wb$vectors[, 1:2])
c_wb <- as.matrix(iris[, 1:4]) %*% vep_wb

plot(c_wb,
  col = iris$Y, pch = 19, main = "AFD (W^(-1)B) - Projection sur les 2 premiers axes",
  xlab = "Discriminant 1", ylab = "Discriminant 2"
)
legend("topright", legend = levels(iris$Y), col = 1:3, pch = 19)

# 2.2.5 Comparaison ACP vs AFD
cat("\n--- Comparaison ACP vs AFD ---\n")
cat(
  "En ACP, les deux premiers axes expliquent", round(sum(pct_inertie_acp[1:2]), 2),
  "% de l'inertie totale.\n"
)
cat("Cependant l'ACP maximise la variance totale, sans tenir compte des classes.\n")
cat("En AFD, les axes discriminants maximisent la séparation entre classes :\n")
cat("  - d1 : R² =", round(r2_d1 * 100, 2), "% de variance expliquée par la classe\n")
cat("  - d2 : R² =", round(r2_d2 * 100, 2), "% de variance expliquée par la classe\n")
cat("L'AFD sépare donc beaucoup mieux les groupes que l'ACP, car elle\n")
cat("cherche explicitement à maximiser le rapport variance inter / variance intra.\n")
cat("Sur le graphique AFD, les 3 espèces sont nettement mieux séparées que sur l'ACP.\n")

# 2.3 Calcul des scores discriminants

cat("\n\n2.3 Calcul des scores discriminants\n\n")

# Calcul des coefficients de score pour chaque groupe
# alpha_i0 = -X_i' W^(-1) X_i
# alpha_ij = 2 * (W^(-1) X_i)_j

W_inv <- solve(W)

# Centres des classes (moyennes par groupe)
centres <- by(iris[, 1:4], iris$Y, colMeans)

# Calcul des coefficients
coefficients <- matrix(0, nrow = 5, ncol = 3)
rownames(coefficients) <- c("alpha0", "X1", "X2", "X3", "X4")
colnames(coefficients) <- names(centres)

for (i in 1:3) {
  xi <- centres[[i]]
  # alpha_i0
  coefficients[1, i] <- -t(xi) %*% W_inv %*% xi
  # alpha_ij (j = 1..4)
  coefficients[2:5, i] <- 2 * (W_inv %*% xi)
}

cat("Tableau des coefficients de score:\n")
print(coefficients)

# Calcul des scores pour tous les individus
# On ajoute une colonne de 1 pour le terme constant
X_avec_constante <- cbind(1, as.matrix(iris[, 1:4]))

# Scores pour chaque groupe
scores <- X_avec_constante %*% coefficients
colnames(scores) <- names(centres)

cat("\nScores des individus (premières lignes):\n")
print(head(scores))

# Classement des individus (groupe avec le score maximal)
classement <- apply(scores, 1, which.max)
classement <- factor(classement, levels = 1:3, labels = levels(iris$Y))

cat("\nClassement des individus (premiers):\n")
print(head(classement))

# Table de confusion
cat("\nTable de confusion:\n")
print(table(Predit = classement, Reel = iris$Y))

# Taux de bon classement
taux_bon <- mean(classement == iris$Y)
cat("\nTaux de bon classement:", taux_bon * 100, "%\n")

# =====================================================
# Résumé des résultats
# =====================================================

cat("\n========================================\n")
cat("RESUME DES RESULTATS\n")
cat("========================================\n")
cat("\n1. Analyse préliminaire:\n")
cat("   - Variable la mieux expliquée:", best_var, "(R² =", round(max(allr2), 4), ")\n")
cat("\n2. AFD:\n")
cat("   - V = W + B : Vérifié\n")
cat("   - Relation mu = lambda/(1-lambda) : Vérifiée\n")
cat("   - Variance d1 expliquée par classe:", round(r2_d1 * 100, 2), "%\n")
cat("   - Variance d2 expliquée par classe:", round(r2_d2 * 100, 2), "%\n")
cat("\n3. Scores discriminants:\n")
cat("   - Taux de bon classement:", round(taux_bon * 100, 2), "%\n")
