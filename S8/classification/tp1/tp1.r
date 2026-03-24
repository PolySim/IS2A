library(ggplot2)
library(tidyr)
library(dplyr)

# 1

# 1.1

data("iris")

iris <- iris %>%
  rename(
    X1 = Sepal.Length,
    X2 = Sepal.Width,
    X3 = Petal.Length,
    X4 = Petal.Width,
    Y = Species
  )

print(head(iris))

# 1.2

p <- iris %>%
  gather("variable", "mesure", -Y) %>%
  ggplot(aes(x = Y, y = mesure, col = Y)) +
  geom_boxplot() +
  facet_wrap(~variable, scales = "free_y") +
  ggtitle("Distribution des variables par espèce")

print(p)

# 1.3

colors <- c("setosa" = "red", "versicolor" = "green", "virginica" = "blue")
pairs(iris[, 1:4],
  col = colors[iris$Y], pch = 19,
  main = "Matrice de dispersion par espèce"
)

library(GGally)

print(ggpairs(iris,
  columns = 1:4, aes(color = Y),
  title = "Matrice de dispersion avec ggpairs"
))

# 1.4

allanov <- lapply(1:4, FUN = function(i) {
  anova(lm(get(paste0("X", i)) ~ Y, data = iris))
})

allpval <- sapply(allanov, FUN = function(x) x$`Pr(>F)`[1])
cat("\nP-values des ANOVAs:\n")
print(allpval)

allr2 <- sapply(summary(lm(cbind(X1, X2, X3, X4) ~ Y, data = iris)),
  FUN = function(x) x$r.squared
)
cat("\nR2 pour chaque variable:\n")
print(allr2)

best_var <- names(allr2)[which.max(allr2)]
cat("\nVariable la mieux expliquée par Y:", best_var, "(R2=", max(allr2), ")\n")


# 2

# 2.1

v_result <- cov.wt(iris[, 1:4], method = "ML")
v <- v_result$cov
cat("V - Matrice de covariance globale:\n")
print(v)

# Effectifs par groupe
n_i <- table(iris$Y)
n <- nrow(iris)

w_list <- by(iris[, 1:4], iris$Y, function(x) cov.wt(x, method = "ML")$cov)
cat("\nMatrices de covariance intra-groupe:\n")
for (i in seq_along(w_list)) {
  cat("\nW", i, "(", names(w_list)[i], "):\n", sep = "")
  print(w_list[[i]])
}

w <- (n_i[1] * w_list[[1]] + n_i[2] * w_list[[2]] + n_i[3] * w_list[[3]]) / n
cat("\nW - Matrice de covariance intra-groupe (pondérée):\n")
print(w)

# Matrice de covariance inter-groupe B
g <- by(iris[, 1:4], iris$Y, colMeans)
g_matrix <- do.call(rbind, g)
cat("\nCentres des classes (G):\n")
print(g_matrix)

x_bar <- colMeans(iris[, 1:4])
cat("\nMoyenne globale:\n")
print(x_bar)

b <- cov.wt(g_matrix, wt = as.numeric(n_i), method = "ML")$cov
cat("\nB - Matrice de covariance inter-groupe:\n")
print(b)

cat("\nVérification V = W + B:\n")
cat("V:\n")
print(v)
cat("\nW + B:\n")
print(w + b)
cat("\nDifférence max :", max(abs(v - (w + b))), "\n")

# 2.2

acp <- eigen(v)$vectors
c_acp <- as.matrix(iris[, 1:4]) %*% acp[, 1:2]

val_propres_acp <- eigen(v)$values
pct_inertie_acp <- val_propres_acp / sum(val_propres_acp) * 100
cat("Pourcentage d'inertie expliquée par axe:\n")
cat("Axe 1:", pct_inertie_acp[1], "%\n")
cat("Axe 2:", pct_inertie_acp[2], "%\n")
cat("Deux premiers axes:", sum(pct_inertie_acp[1:2]), "%\n")

plot(c_acp,
  col = iris$Y, pch = 19, main = "ACP - Projection sur les 2 premiers axes",
  xlab = "Composante 1", ylab = "Composante 2"
)

v_inv_b <- solve(v) %*% b
afd_vb <- eigen(v_inv_b)

lambda <- Re(afd_vb$values[1:4])
cat("Valeurs propres de V^(-1)B:\n")
print(lambda)

vep_vb <- Re(afd_vb$vectors[, 1:2])

c_vb <- as.matrix(iris[, 1:4]) %*% vep_vb
colnames(c_vb) <- c("d1", "d2")

cat("\nCoordonnées des points projetés (premières lignes):\n")
print(head(c_vb))

plot(c_vb,
  col = iris$Y, pch = 19,
  main = "AFD (V^(-1)B) - Projection sur les 2 premiers axes",
  xlab = "Discriminant 1", ylab = "Discriminant 2"
)

r2_d1 <- summary(lm(c_vb[, 1] ~ iris$Y))$r.squared
cat("\nPart de variance de d1 expliquée par la classe:", r2_d1 * 100, "%\n")

r2_d2 <- summary(lm(c_vb[, 2] ~ iris$Y))$r.squared
cat("Part de variance de d2 expliquée par la classe:", r2_d2 * 100, "%\n")

cat("\n--- AFD avec W^(-1)B ---\n")
w_inv_b <- solve(w) %*% b
afd_wb <- eigen(w_inv_b)

mu <- Re(afd_wb$values[1:4])
cat("Valeurs propres de W^(-1)B (mu):\n")
print(mu)

cat("\nVérification de la relation mu = lambda / (1 - lambda):\n")
mu_calc <- lambda / (1 - lambda)
cat("mu calculé:", mu_calc, "\n")
cat("mu obtenu:", mu[1:4], "\n")
cat("Différence:", max(abs(mu[1:4] - mu_calc)), "\n")

# Vecteurs propres et coordonnées
vep_wb <- Re(afd_wb$vectors[, 1:2])
c_wb <- as.matrix(iris[, 1:4]) %*% vep_wb

plot(c_wb,
  col = iris$Y, pch = 19,
  main = "AFD (W^(-1)B) - Projection sur les 2 premiers axes",
  xlab = "Discriminant 1",
  ylab = "Discriminant 2"
)

# 2.3

w_inv <- solve(w)

centres <- by(iris[, 1:4], iris$Y, colMeans)

coefficients <- matrix(0, nrow = 5, ncol = 3)
rownames(coefficients) <- c("alpha0", "X1", "X2", "X3", "X4")
colnames(coefficients) <- names(centres)

for (i in 1:3) {
  xi <- centres[[i]]
  # alpha_i0
  coefficients[1, i] <- -t(xi) %*% w_inv %*% xi
  # alpha_ij (j = 1..4)
  coefficients[2:5, i] <- 2 * (w_inv %*% xi)
}

cat("Tableau des coefficients de score:\n")
print(coefficients)

x_avec_constante <- cbind(1, as.matrix(iris[, 1:4]))

scores <- x_avec_constante %*% coefficients
colnames(scores) <- names(centres)

cat("\nScores des individus (premières lignes):\n")
print(head(scores))

classement <- apply(scores, 1, which.max)
classement <- factor(classement, levels = 1:3, labels = levels(iris$Y))

cat("\nClassement des individus (premiers):\n")
print(head(classement))

cat("\nTable de confusion:\n")
print(table(Predit = classement, Reel = iris$Y))

taux_bon <- mean(classement == iris$Y)
cat("\nTaux de bon classement:", taux_bon * 100, "%\n")
