data <- read.csv("/Users/simondesdevises/Documents/IS2A/S6/StatInf/TP3/sinistres.csv") # nolint: line_length_linter.
x <- data[[2]]

# Question 1

# Création d'une matrice pour stocker les 1000 échantillons de taille 30
echantillons <- matrix(0, nrow = 30, ncol = 1000)

# Génération des échantillons
set.seed(123)  # Pour la reproductibilité
for (i in 1:1000) {
  echantillons[, i] <- sample(x, size = 30, replace = TRUE)
}

# Question 2

# Fonction pour tester la nullité de la moyenne
# H0 : μ = 0 vs H1 : μ ≠ 0
test_nullite_moyenne <- function(echantillon, sigma, alpha) {
  n <- length(echantillon)
  moyenne <- mean(echantillon)

  # Statistique de test
  z <- (moyenne) / (sigma / sqrt(n))

  # Valeur critique (test bilatéral)
  z_critique <- qnorm(1 - alpha / 2)

  # Retourne 1 si H0 est rejetée (|z| > z_critique), 0 sinon
  as.numeric(abs(z) > z_critique)
}

# Question 3

# Calcul de l'écart-type de la population
sigma <- sd(x)

# Test pour chaque échantillon (H0 : μ = 650)
resultats_tests <- numeric(1000)
for (i in 1:1000) {
  # On centre les données en soustrayant 650 pour tester H0 : μ = 650
  echantillon_centre <- echantillons[, i] - 650
  resultats_tests[i] <- test_nullite_moyenne(echantillon_centre, sigma, 0.05)
}

# Nombre de rejets de H0
nombre_rejets <- sum(resultats_tests)
cat("Nombre de fois où H0 (μ = 650) est rejetée:", nombre_rejets, "sur 1000 tests\n") # nolint: line_length_linter.
cat("Proportion de rejets:", round(nombre_rejets / 1000 * 100, 2), "%\n")

# Question 4

# Formule explicite de la p-valeur :
# Pour un test bilatéral H0 : μ = μ0 vs H1 : μ ≠ μ0
# Statistique de test : Z = (X̄ - μ0) / (σ/√n)
# Sous H0, Z ~ N(0,1)
# p-valeur = 2 × P(Z > |z_obs|) = 2 × (1 - Φ(|z_obs|))
# où Φ est la fonction de répartition de la loi normale centrée réduite

pvaleur_test <- function(echantillon, sigma, mu0 = 0) {
  n <- length(echantillon)
  moyenne <- mean(echantillon)

  # Statistique de test
  z <- (moyenne - mu0) / (sigma / sqrt(n))

  # p-valeur pour un test bilatéral
  pvaleur <- 2 * (1 - pnorm(abs(z)))

  return(pvaleur)
}

# Question 5

# Formule explicite de la puissance :
# Pour un test bilatéral H0 : μ = μ0 vs H1 : μ = μ1
# Sous H0 : Z = (X̄ - μ0) / (σ/√n) ~ N(0,1)
# Sous H1 : Z = (X̄ - μ0) / (σ/√n) ~ N(δ√n/σ, 1) où δ = μ1 - μ0
#
# Région de rejet : |Z| > z_{α/2}
# Puissance = P(rejeter H0 | H1 vraie)
#          = P(|Z| > z_{α/2} | μ = μ1)
#          = P(Z > z_{α/2} | μ = μ1) + P(Z < -z_{α/2} | μ = μ1)
#          = 1 - Φ(z_{α/2} - δ√n/σ) + Φ(-z_{α/2} - δ√n/σ)

puissance_test <- function(n, sigma, alpha, delta) {
  # Valeur critique pour un test bilatéral
  z_alpha_2 <- qnorm(1 - alpha / 2)

  # Paramètre de non-centralité
  lambda <- delta * sqrt(n) / sigma

  # Puissance du test bilatéral
  puissance <- 1 - pnorm(z_alpha_2 - lambda) + pnorm(-z_alpha_2 - lambda)

  puissance
}

# Question 7

# Installation et chargement du package pwr si nécessaire
if (!require(pwr)) {
  install.packages("pwr")
  library(pwr)
}

# Question 5 avec pwr.norm.test
cat("\n=== Question 5 avec pwr.norm.test ===\n")

# Exemple de calcul de puissance avec des paramètres donnés
# Par exemple : n=30, sigma=sigma, alpha=0.05, delta=20
exemple_puissance <- pwr.norm.test(d = 20 / sigma, n = 30, sig.level = 0.05,
                                   alternative = "two.sided")
cat("Puissance pour n=30, delta=20, alpha=5% :", round(exemple_puissance$power, 4), "\n") # nolint: line_length_linter.

# Vérification avec notre fonction
puissance_manuelle <- puissance_test(30, sigma, 0.05, 20)
cat("Puissance avec notre fonction :", round(puissance_manuelle, 4), "\n")

# Question 6

# Paramètres
alpha <- 0.05
delta_values <- seq(-50, 50, by = 1)
n_values <- c(30, 50, 100)

# Calcul des puissances pour chaque combinaison (n, δ)
puissances <- matrix(0, nrow = length(delta_values), ncol = length(n_values))

for (i in seq_along(n_values)) {
  for (j in seq_along(delta_values)) {
    puissances[j, i] <- puissance_test(n_values[i], sigma, alpha, delta_values[j]) # nolint: line_length_linter.
  }
}

# Création du graphique
plot(delta_values, puissances[, 1], type = "l", col = "blue", lwd = 2,
     xlab = "δ (différence μ1 - μ0)", ylab = "Puissance du test",
     main = "Puissance du test en fonction de δ (α = 5%)",
     ylim = c(0, 1))

lines(delta_values, puissances[, 2], col = "red", lwd = 2)
lines(delta_values, puissances[, 3], col = "green", lwd = 2)

# Ajout d'une ligne horizontale pour α = 0.05
abline(h = alpha, lty = 2, col = "gray")

# Légende
legend("bottomright",
       legend = c("n = 30", "n = 50", "n = 100", "α = 5%"),
       col = c("blue", "red", "green", "gray"),
       lty = c(1, 1, 1, 2),
       lwd = c(2, 2, 2, 1))

# Question 6 avec pwr.norm.test
cat("\n=== Question 6 avec pwr.norm.test ===\n")

# Calcul des puissances avec pwr pour les mêmes paramètres
puissances_pwr <- matrix(0, nrow = length(delta_values), ncol = length(n_values)) # nolint: line_length_linter.

for (i in seq_along(n_values)) {
  for (j in seq_along(delta_values)) {
    # pwr.norm.test utilise l'effect size d = delta/sigma
    effect_size <- delta_values[j] / sigma
    puissance_pwr <- pwr.norm.test(d = effect_size, n = n_values[i],
                                   sig.level = alpha, alternative = "two.sided")
    puissances_pwr[j, i] <- puissance_pwr$power
  }
}

# Création du graphique avec pwr
plot(delta_values, puissances_pwr[, 1], type = "l", col = "blue", lwd = 2,
     xlab = "δ (différence μ1 - μ0)", ylab = "Puissance du test",
     main = "Puissance du test avec pwr.norm.test (α = 5%)",
     ylim = c(0, 1))

lines(delta_values, puissances_pwr[, 2], col = "red", lwd = 2)
lines(delta_values, puissances_pwr[, 3], col = "green", lwd = 2)

# Ajout d'une ligne horizontale pour α = 0.05
abline(h = alpha, lty = 2, col = "gray")

# Légende
legend("bottomright",
       legend = c("n = 30", "n = 50", "n = 100", "α = 5%"),
       col = c("blue", "red", "green", "gray"),
       lty = c(1, 1, 1, 2),
       lwd = c(2, 2, 2, 1))

# Comparaison des résultats
cat("\nComparaison des puissances (delta=20, alpha=5%) :\n")
for (i in seq_along(n_values)) {
  puissance_manuelle <- puissance_test(n_values[i], sigma, alpha, 20)
  puissance_pwr_val <- pwr.norm.test(d = 20 / sigma, n = n_values[i],
                                     sig.level = alpha, alternative = "two.sided")$power # nolint: line_length_linter.
  cat("n =", n_values[i], ": Manuel =", round(puissance_manuelle, 4),
      ", pwr =", round(puissance_pwr_val, 4), "\n")
}

# Question 8

# Calcul de la taille d'échantillon nécessaire
# Objectif : β ≤ 10% donc puissance ≥ 90%
# Paramètres : α = 5%, δ = 20, puissance minimale = 90%

alpha <- 0.05
delta <- 20
puissance_minimale <- 0.90  # 1 - β = 1 - 0.10

cat("\n=== Question 8 : Calcul de la taille d'échantillon ===\n")
cat("Objectifs :\n")
cat("- α =", alpha, "\n")
cat("- δ =", delta, "\n")
cat("- Risque de seconde espèce β ≤ 10%\n")
cat("- Puissance minimale =", puissance_minimale, "\n\n")

# Méthode 1 : Avec pwr.norm.test
effect_size <- delta / sigma
n_necessaire_pwr <- pwr.norm.test(d = effect_size,
                                  sig.level = alpha,
                                  power = puissance_minimale,
                                  alternative = "two.sided")

cat("Résultat avec pwr.norm.test :\n")
cat("Taille d'échantillon nécessaire :", ceiling(n_necessaire_pwr$n), "\n")
cat("Taille exacte :", round(n_necessaire_pwr$n, 2), "\n\n")

# Méthode 2 : Recherche par itération avec notre fonction
n_test <- 1
while (puissance_test(n_test, sigma, alpha, delta) < puissance_minimale) {
  n_test <- n_test + 1
}

cat("Vérification avec notre fonction :\n")
cat("Taille d'échantillon nécessaire :", n_test, "\n")
cat("Puissance obtenue :", round(puissance_test(n_test, sigma, alpha, delta), 4), "\n") # nolint: line_length_linter.

# Vérification avec n-1 pour confirmer
if (n_test > 1) {
  puissance_n_moins_1 <- puissance_test(n_test - 1, sigma, alpha, delta)
  cat("Puissance avec n =", n_test - 1, ":", round(puissance_n_moins_1, 4),
      "(insuffisante)\n")
}

cat("\nConclusion : Il faut au minimum", n_test, "observations pour que le risque\n") # nolint: line_length_linter.
cat("de seconde espèce ne dépasse pas 10% avec δ = 20 et α = 5%.\n")