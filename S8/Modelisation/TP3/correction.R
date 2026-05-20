#############################################################################
# CORRECTION TP - Analyse de survie avec le jeu de données `lung`
#############################################################################

# Chargement des packages nécessaires
if (!require("survival")) install.packages("survival")
if (!require("swimplot")) install.packages("swimplot")
if (!require("glmnet")) install.packages("glmnet")

library(survival)
library(swimplot)
library(glmnet)

# ---------------------------------------------------------------------------
# QUESTION 1 : Charger les données `lung` et analyse descriptive
# ---------------------------------------------------------------------------
data(lung)

# Vue d'ensemble du jeu de données
str(lung)
summary(lung)

# Quelques statistiques descriptives supplémentaires
head(lung)
table(lung$sex)          # 1 = Homme, 2 = Femme
table(lung$status)       # 1 = censuré, 2 = décès
table(lung$ph.ecog, useNA = "ifany")

# Remarque : le jeu contient 228 patients, des données manquantes pour `ph.ecog`
# et `meal.cal`, et une variable de censure `status` (1 = censuré, 2 = décès).


# ---------------------------------------------------------------------------
# QUESTION 2 : Graphe de suivi des individus (swimmer plot)
# ---------------------------------------------------------------------------
# Préparation : identifier le type d'événement
lung$event_type <- ifelse(lung$status == 2, "Décès", "Censure")

# swimmer_plot basique
swimmer_plot(df = lung,
             id = "inst",
             end = "time",
             fill = "lightblue")

# Ajout des points pour les événements
swimmer_plot(df = lung,
             id = "inst",
             end = "time",
             fill = "lightblue") +
  swimmer_points(df_points = lung,
                 id = "inst",
                 time = "time",
                 name_shape = "event_type",
                 size = 2)

# REMARQUES :
# - On observe beaucoup plus de points correspondant à des "Censure" que de "Décès".
# - Cela signifie qu'une proportion importante des patients est encore en vie
#   (ou perdue de vue) à la fin de l'étude : il y a une forte censure à droite.


# ---------------------------------------------------------------------------
# QUESTION 3 : Temps moyen avant décès chez les femmes
# ---------------------------------------------------------------------------
# Filtrer les femmes (sex == 2) avec un événement de décès (status == 2)
temps_deces_femmes <- lung$time[lung$sex == 2 & lung$status == 2]
temps_moyen_deces_femmes <- mean(temps_deces_femmes)
cat("Temps moyen avant décès chez les femmes :", round(temps_moyen_deces_femmes, 2), "jours\n")


# ---------------------------------------------------------------------------
# QUESTION 4 : Courbe de Kaplan-Meier pour l'ensemble des patients
# ---------------------------------------------------------------------------
km_all <- survfit(Surv(time, status) ~ 1, data = lung)
plot(km_all, xlab = "Temps (jours)", ylab = "Probabilité de survie",
     main = "Courbe de Kaplan-Meier - Tous les patients",
     conf.int = TRUE)

# REMARQUES :
# - La courbe est décroissante en escalier : à chaque événement (décès),
#   la probabilité de survie diminue.
# - On peut lire la médiane de survie (temps auquel S(t) = 0.5) :
print(km_all)
# - La survie à 1 an (~365 jours) est d'environ 40-45 %.


# ---------------------------------------------------------------------------
# QUESTION 5 : Courbe de Kaplan-Meier selon le sexe
# ---------------------------------------------------------------------------
km_sex <- survfit(Surv(time, status) ~ sex, data = lung)
plot(km_sex, xlab = "Temps (jours)", ylab = "Probabilité de survie",
     main = "Courbe de Kaplan-Meier selon le sexe",
     col = c("blue", "red"), lty = 1:2)
legend("topright", legend = c("Hommes (1)", "Femmes (2)"),
       col = c("blue", "red"), lty = 1:2)

# REMARQUES :
# - La courbe des femmes (sex = 2) est systématiquement au-dessus de celle des hommes.
# - Cela indique que les femmes ont une meilleure survie que les hommes dans cette cohorte.
# - Le sexe semble donc être un facteur pronostique de survie.


# ---------------------------------------------------------------------------
# QUESTION 6 : Modèle de Cox simple (age + sex)
# ---------------------------------------------------------------------------
# Suppression des lignes avec NA pour ph.ecog afin de comparer les modèles
# sur le même jeu de données (requis pour anova)
lung_complete <- lung[!is.na(lung$ph.ecog), ]

cox_mod1 <- coxph(Surv(time, status) ~ age + sex, data = lung_complete)
summary(cox_mod1)

# INTERPRÉTATION :
# - age : HR > 1 significatif (si p < 0.05). Le risque de décès augmente avec l'âge.
#   Par exemple, si HR(age) = 1.02, le risque relatif augmente de 2 % par année d'âge.
# - sex : HR < 1 significatif. Les femmes (sex=2 vs sex=1) ont un risque diminué
#   par rapport aux hommes. Par exemple, si HR(sex) = 0.60, le risque est réduit de 40 %.
# - Les intervalles de confiance à 95 % ne doivent pas contenir 1 pour que l'effet
#   soit statistiquement significatif.


# ---------------------------------------------------------------------------
# QUESTION 7 : Ajout de ph.ecog et comparaison par ANOVA (test du RV)
# ---------------------------------------------------------------------------
cox_mod2 <- coxph(Surv(time, status) ~ age + sex + ph.ecog,
                  data = lung_complete)
summary(cox_mod2)

# Test du rapport de vraisemblance
anova(cox_mod1, cox_mod2, test = "Chisq")

# DÉDUCTION :
# - Si la p-value du test est < 0.05, l'ajout de `ph.ecog` améliore
#   significativement l'ajustement du modèle.
# - Cela signifie que le statut ECOG est un prédicteur pertinent de la survie
#   indépendamment de l'âge et du sexe.


# ---------------------------------------------------------------------------
# QUESTION 8 : Prédiction du risque pour un patient cible
# ---------------------------------------------------------------------------
patient_cible <- data.frame(age = 60, sex = 1, ph.ecog = 1)

# Prédiction du risque relatif (hazard ratio par rapport au profil de référence)
risque_relatif <- predict(cox_mod2, newdata = patient_cible, type = "risk")
cat("Risque relatif prédit pour le patient cible :", round(risque_relatif, 3), "\n")

# Prédiction de la survie à différents horizons
surv_pred <- survfit(cox_mod2, newdata = patient_cible)
print(surv_pred)


# ---------------------------------------------------------------------------
# QUESTION 9 : Vérification de l'hypothèse de proportionnalité des risques
# ---------------------------------------------------------------------------
cox_zph <- cox.zph(cox_mod2)
print(cox_zph)
plot(cox_zph)

# INTERPRÉTATION :
# - Si p-value globale ou pour une variable < 0.05, l'hypothèse de proportionnalité
#   est rejetée pour cette variable.
# - Les résidus de Schoenfeld ne doivent pas montrer de tendance systématique
#   en fonction du temps.


# ---------------------------------------------------------------------------
# QUESTION 10 : Modèle de Cox stratifié (si `age` viole la PH)
# ---------------------------------------------------------------------------
# On crée des classes d'âge pour pouvoir stratifier
lung$age_group <- cut(lung$age,
                      breaks = c(0, 60, 70, Inf),
                      labels = c("<=60", "61-70", ">70"))

# Modèle de Cox stratifié par groupe d'âge
cox_strat <- coxph(Surv(time, status) ~ sex + ph.ecog + strata(age_group),
                   data = lung)
summary(cox_strat)

# INTERPRÉTATION :
# - Le modèle est ajusté séparément dans chaque strate d'âge.
# - Les coefficients de `sex` et `ph.ecog` sont supposés constants entre strates,
#   mais le risque de base (baseline hazard) peut différer.


# ---------------------------------------------------------------------------
# QUESTION 11 : C-index du modèle de Cox
# ---------------------------------------------------------------------------
# On reprend le modèle avec age + sex + ph.ecog (question 7)
# Le C-index est directement fourni dans le summary :
cat("C-index du modèle de Cox (question 7) :", summary(cox_mod2)$concordance["C"], "\n")

# Alternative avec survConcordance (ancienne fonction)
# conc <- survConcordance(Surv(time, status) ~ predict(cox_mod2, type = "risk"), data = lung)
# conc$concordance


# ---------------------------------------------------------------------------
# QUESTION 12 : Estimation du C-index par cross-validation
# ---------------------------------------------------------------------------
set.seed(123)
k <- 5  # nombre de plis
n <- nrow(lung)
folds <- sample(rep(1:k, length.out = n))

c_index_cv <- numeric(k)

for (i in 1:k) {
  # Séparation entraînement / test
  train_idx <- which(folds != i)
  test_idx <- which(folds == i)
  
  # Ajustement sur le jeu d'entraînement
  cox_train <- coxph(Surv(time, status) ~ age + sex + ph.ecog,
                     data = lung, subset = train_idx)
  
  # Prédictions sur le jeu de test
  lp_test <- predict(cox_train, newdata = lung[test_idx, ], type = "lp")
  
  # Calcul du C-index avec Cindex de glmnet
  y_test <- Surv(lung$time[test_idx], lung$status[test_idx])
  c_index_cv[i] <- Cindex(pred = lp_test, y = y_test)
}

cat("C-index par pli :", round(c_index_cv, 3), "\n")
cat("C-index moyen en", k, "-fold CV :", round(mean(c_index_cv), 3), "\n")

#############################################################################
# FIN DE LA CORRECTION
#############################################################################
