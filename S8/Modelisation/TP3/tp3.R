library(survival)
library(swimplot)
library(glmnet)

# QUESTION 1
data(lung)

print("str")
str(lung)
print("summary")
summary(lung)
print("head")
head(lung)
print("table sex")
table(lung$sex)
print("table status")
table(lung$status)
print("table ph.ecog")
table(lung$ph.ecog, useNA = "ifany")

# QUESTION 2

lung$event_type <- ifelse(lung$status == 2, "Décès", "Censure")

swimmer_plot(df = lung,
             id = "inst",
             end = "time",
             fill = "lightblue")

swimmer_plot(df = lung,
             id = "inst",
             end = "time",
             fill = "lightblue") +
  swimmer_points(df_points = lung,
                 id = "inst",
                 time = "time",
                 name_shape = "event_type",
                 size = 2)

# QUESTION 3

temps_deces_femmes <- lung$time[lung$sex == 2 & lung$status == 2]
temps_moyen_deces_femmes <- mean(temps_deces_femmes)
cat(
  "Temps moyen avant décès chez les femmes :",
  round(temps_moyen_deces_femmes, 2), "jours\n"
)


# QUESTION 4

km_all <- survfit(Surv(time, status) ~ 1, data = lung)
plot(km_all, xlab = "Temps (jours)", ylab = "Probabilité de survie",
     main = "Courbe de Kaplan-Meier - Tous les patients",
     conf.int = TRUE)


# QUESTION 5

km_sex <- survfit(Surv(time, status) ~ sex, data = lung)
plot(km_sex, xlab = "Temps (jours)", ylab = "Probabilité de survie",
     main = "Courbe de Kaplan-Meier selon le sexe",
     col = c("blue", "red"), lty = 1:2)
legend("topright", legend = c("Hommes (1)", "Femmes (2)"),
       col = c("blue", "red"), lty = 1:2)


# QUESTION 6
# On utilise un sous-jeu sans NA dans ph.ecog pour que les deux modèles
# soient ajustés sur le même nombre d'observations (requis pour anova)
lung_complete <- lung[!is.na(lung$ph.ecog), ]

cox_mod1 <- coxph(Surv(time, status) ~ age + sex, data = lung_complete)
summary(cox_mod1)

# QUESTION 7

cox_mod2 <- coxph(Surv(time, status) ~ age + sex + ph.ecog,
                  data = lung_complete)
summary(cox_mod2)

anova(cox_mod1, cox_mod2, test = "Chisq")


# QUESTION 8

patient_cible <- data.frame(age = 60, sex = 1, ph.ecog = 1)

risque_relatif <- predict(cox_mod2, newdata = patient_cible, type = "risk")
cat(
  "Risque relatif prédit pour le patient cible :",
  round(risque_relatif, 3), "\n"
)

surv_pred <- survfit(cox_mod2, newdata = patient_cible)
print(surv_pred)


# QUESTION 9

cox_zph <- cox.zph(cox_mod2)
print(cox_zph)
plot(cox_zph)

# QUESTION 10

lung$age_group <- cut(lung$age,
                      breaks = c(0, 60, 70, Inf),
                      labels = c("<=60", "61-70", ">70"))

cox_strat <- coxph(Surv(time, status) ~ sex + ph.ecog + strata(age_group),
                   data = lung)
summary(cox_strat)

# QUESTION 11

cat(
  "C-index du modèle de Cox (question 7) :",
  summary(cox_mod2)$concordance["C"], "\n"
)

# QUESTION 12

set.seed(123)
k <- 5
n <- nrow(lung)
folds <- sample(rep(1:k, length.out = n))

c_index_cv <- numeric(k)

for (i in 1:k) {
  train_idx <- which(folds != i)
  test_idx <- which(folds == i)

  cox_train <- coxph(Surv(time, status) ~ age + sex + ph.ecog,
                     data = lung, subset = train_idx)

  lp_test <- predict(cox_train, newdata = lung[test_idx, ], type = "lp")

  y_test <- Surv(lung$time[test_idx], lung$status[test_idx])
  c_index_cv[i] <- Cindex(pred = lp_test, y = y_test)
}

cat("C-index par pli :", round(c_index_cv, 3), "\n")
cat("C-index moyen en", k, "-fold CV :", round(mean(c_index_cv), 3), "\n")
