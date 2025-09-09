# Question 1

data <- read.csv("/Users/simondesdevises/Documents/IS2A/S6/StatInf/TP2/poids.csv") # nolint: line_length_linter.

moy <- mean(data[[1]])
var <- sum((data[[1]] - moy)^2) / (length(data[[1]]))

# Question 2

var_mean_corriger <- function(data, n) {
  x <- sample(data, n)
  moy_emp <- mean(x)
  var_emp <- var(x)

  list(
    echantillon = x,
    moyenne = moy_emp,
    variance = var_emp
  )
}

# Tests avec différentes tailles d'échantillons
set.seed(123) # Pour la reproductibilité
res_30 <- var_mean_corriger(data[[1]], 30)
res_60 <- var_mean_corriger(data[[1]], 60)
res_80 <- var_mean_corriger(data[[1]], 80)

# Affichage des résultats
cat("Résultats pour n = 30:\n")
cat("Moyenne:", res_30$moyenne, "\n")
cat("Variance:", res_30$variance, "\n\n")

cat("Résultats pour n = 60:\n")
cat("Moyenne:", res_60$moyenne, "\n")
cat("Variance:", res_60$variance, "\n\n")

cat("Résultats pour n = 80:\n")
cat("Moyenne:", res_80$moyenne, "\n")
cat("Variance:", res_80$variance, "\n")

# Question 3

# Fonction pour calculer l'intervalle de confiance avec variance connue
intervalle_confiance <- function(echantillon, sigma2, alpha = 0.05) {
  n <- length(echantillon)
  moy <- mean(echantillon)
  quantile_d <- qnorm(1 - alpha / 2)
  quantile_g <- qnorm(alpha / 2)

  return(list(
    borne_inf = moy + quantile_g * sqrt(sigma2 / n),
    borne_sup = moy + quantile_d * sqrt(sigma2 / n)
  ))
}

# Calcul des intervalles de confiance pour les différents échantillons
sigma2 <- 438.92^2

ic_30 <- intervalle_confiance(res_30$echantillon, sigma2)
ic_60 <- intervalle_confiance(res_60$echantillon, sigma2)
ic_80 <- intervalle_confiance(res_80$echantillon, sigma2)

# Affichage des résultats
cat("\nIntervalles de confiance à 95% pour la moyenne :\n")
cat("Pour n = 30: [", ic_30$borne_inf, ",", ic_30$borne_sup, "]\n")
cat("Pour n = 60: [", ic_60$borne_inf, ",", ic_60$borne_sup, "]\n")
cat("Pour n = 80: [", ic_80$borne_inf, ",", ic_80$borne_sup, "]\n")

# Question 4

# Fonction pour calculer l'intervalle de confiance avec variance inconnue
intervalle_confiance_student <- function(echantillon, alpha = 0.05) {
  n <- length(echantillon)
  moy <- mean(echantillon)
  var_emp <- var(echantillon)
  quantile_d <- qt(1 - alpha / 2, df = n - 1)
  quantile_g <- qt(alpha / 2, df = n - 1)
  return(list(
    borne_inf = moy + quantile_g * sqrt(var_emp / n),
    borne_sup = moy + quantile_d * sqrt(var_emp / n)
  ))
}

# Calcul des intervalles de confiance pour les différents échantillons
ic_student_30 <- intervalle_confiance_student(res_30$echantillon)
ic_student_60 <- intervalle_confiance_student(res_60$echantillon)
ic_student_80 <- intervalle_confiance_student(res_80$echantillon)

# Comparaison avec t.test
ttest_30 <- t.test(res_30$echantillon, conf.level = 0.95)
ttest_60 <- t.test(res_60$echantillon, conf.level = 0.95)
ttest_80 <- t.test(res_80$echantillon, conf.level = 0.95)

# Affichage des résultats
cat("\nIntervalles de confiance à 95% pour la moyenne (variance inconnue) :\n")
cat("\nNotre fonction :\n")
cat("Pour n = 30: [", ic_student_30$borne_inf, ",", ic_student_30$borne_sup, "]\n") # nolint: line_length_linter.
cat("Pour n = 60: [", ic_student_60$borne_inf, ",", ic_student_60$borne_sup, "]\n") # nolint: line_length_linter.
cat("Pour n = 80: [", ic_student_80$borne_inf, ",", ic_student_80$borne_sup, "]\n") # nolint: line_length_linter.

cat("\nFonction t.test :\n")
cat("Pour n = 30: [", ttest_30$conf.int[1], ",", ttest_30$conf.int[2], "]\n")
cat("Pour n = 60: [", ttest_60$conf.int[1], ",", ttest_60$conf.int[2], "]\n")
cat("Pour n = 80: [", ttest_80$conf.int[1], ",", ttest_80$conf.int[2], "]\n")

# Question 5

# Données du problème
n_total <- 197  # Nombre total de cartons
n_conformes <- 177  # Nombre de cartons conformes
n_defectueux <- n_total - n_conformes  # Nombre de cartons défectueux
p_chapeau <- n_defectueux / n_total  # Proportion empirique de cartons défectueux # nolint: line_length_linter.

# Fonction pour calculer l'intervalle de confiance d'une proportion (méthode du cours) # nolint: line_length_linter.
intervalle_confiance_prop <- function(n, p_chapeau, alpha = 0.05) {
  quantile <- qnorm(1 - alpha / 2)
  marge_erreur <- quantile * sqrt(p_chapeau * (1 - p_chapeau) / n) # nolint: line_length_linter.
  return(list(
    borne_inf = max(0, p_chapeau - marge_erreur),  # On borne à 0 # nolint: line_length_linter.
    borne_sup = min(1, p_chapeau + marge_erreur)   # On borne à 1
  ))
}

# Calcul avec la méthode du cours
ic_prop <- intervalle_confiance_prop(n_total, p_chapeau)

# Calcul avec binom.test
test_binom <- binom.test(n_defectueux, n_total, conf.level = 0.95)

# Affichage des résultats
cat("\nIntervalle de confiance à 95% pour la proportion de cartons défectueux :\n") # nolint: line_length_linter.
cat("\nProportion empirique de cartons défectueux :", p_chapeau, "\n")

cat("\nMéthode du cours (approximation normale) :\n")
cat("[", ic_prop$borne_inf, ",", ic_prop$borne_sup, "]\n")

cat("\nMéthode binom.test (méthode exacte) :\n")
cat("[", test_binom$conf.int[1], ",", test_binom$conf.int[2], "]\n\n")

# Comparaison des largeurs d'intervalles
largeur_cours <- ic_prop$borne_sup - ic_prop$borne_inf
largeur_binom <- test_binom$conf.int[2] - test_binom$conf.int[1]

cat("Largeur de l'intervalle (méthode du cours) :", largeur_cours, "\n")
cat("Largeur de l'intervalle (binom.test) :", largeur_binom, "\n")