# ------------------------------------------------------------
# 1. Installation et chargement des packages
# ------------------------------------------------------------
library(pls)
library(caret)
library(ggplot2)
library(corrplot)


# 2. Chargement et description du jeu de donnees gasoline
data(gasoline)

# - gasoline$octane : variable a expliquer (indice d'octane)
# - gasoline$NIR    : spectres infrarouges (variables explicatives)
#   401 variables spectrales mesurees sur 60 echantillons d'essence

dim(gasoline$NIR)
str(gasoline)

summary(gasoline$octane)
apply(gasoline$NIR, 2, summary)

# Visualisation des spectres
plot(1:401, gasoline$NIR[1, ], type = "l", ylim = range(gasoline$NIR),
     xlab = "Longueur d'onde", ylab = "Absorbance",
     main = "Spectres NIR des echantillons d'essence")
for (i in 2:nrow(gasoline$NIR)) {
  lines(1:401, gasoline$NIR[i, ], col = i)
}

cor_matrix <- cor(gasoline$NIR[, seq(1, 401, by = 20)])
corrplot(cor_matrix, method = "color", type = "upper",
         title = "Matrice de correlation (sous-echantillon)")

# On observe des correlations tres elevees entre les variables spectrales
# (proches de 1), ce qui indique une forte multicolinearte.
# Ce phénomène est typique des données spectrales et justifie l'utilisation
# de methodes comme PCR ou PLS.


# 3. Regression lineaire multiple

X <- as.data.frame(unclass(gasoline$NIR))
colnames(X) <- paste0("X", seq_len(ncol(X)))
df <- data.frame(octane = gasoline$octane, X)

lm_model <- lm(octane ~ ., data = df)
summary(lm_model)

# 4. ACP sur les variables explicatives

pca_model <- prcomp(gasoline$NIR, center = TRUE, scale. = FALSE)


# 5. Analyse de l'ACP et nombre d'axes a retenir

summary(pca_model)

plot(pca_model, type = "l", main = "Scree Plot - ACP")

var_explained <- cumsum(pca_model$sdev^2) / sum(pca_model$sdev^2)
plot(var_explained, type = "b", pch = 19,
     xlab = "Nombre de composantes", ylab = "Variance expliquee cumulee",
     main = "Variance expliquee cumulee par l'ACP")
abline(h = 0.95, col = "red", lty = 2)
abline(h = 0.99, col = "blue", lty = 2)
legend("bottomright", legend = c("95%", "99%"), col = c("red", "blue"), lty = 2)

# 6. Ajustement d'un modele PCR

pcr_model <- pcr(octane ~ NIR, ncomp = 20, data = gasoline, validation = "CV")

# 7. Exploration du modele PCR
#
summary(pcr_model)

plot(pcr_model, plottype = "coef", ncomp = 1:5, legendpos = "bottomleft",
     main = "Coefficients PCR")

plot(pcr_model, plottype = "validation", val.type = "RMSEP",
     main = "RMSEP en validation croisee - PCR")


validation_stats <- RMSEP(pcr_model, estimate = "CV")
best_ncomp_pcr <- which.min(validation_stats$val[1, 1, ]) - 1
print(paste("Nombre optimal de composantes PCR :", best_ncomp_pcr))

# 8. MSE du modele PCR

pred_pcr <- predict(pcr_model, ncomp = best_ncomp_pcr, newdata = gasoline)
mse_pcr <- mean((gasoline$octane - pred_pcr)^2)
print(
  paste(
    "MSE PCR (apprentissage, ncomp =", best_ncomp_pcr, ") :", round(mse_pcr, 4)
  )
)

msep_pcr <- validation_stats$val[1, 1, best_ncomp_pcr + 1]^2
print(
  paste(
    "MSEP PCR (validation croisee, ncomp =", best_ncomp_pcr, ") :",
    round(msep_pcr, 4)
  )
)

# 9. Residus du modele PCR
resid_pcr <- gasoline$octane - pred_pcr

par(mfrow = c(2, 2))

plot(pred_pcr, resid_pcr, pch = 19, col = "blue",
     xlab = "Valeurs predites", ylab = "Residus",
     main = "Residus vs Valeurs predites (PCR)")
abline(h = 0, col = "red", lty = 2)

qqnorm(resid_pcr, main = "QQ-plot des residus (PCR)")
qqline(resid_pcr, col = "red")

hist(resid_pcr, breaks = 15, col = "lightblue", border = "white",
     main = "Distribution des residus (PCR)", xlab = "Residus")

plot(
     seq_along(resid_pcr), resid_pcr, type = "b", pch = 19, col = "blue",
     xlab = "Index", ylab = "Residus",
     main = "Residus vs Index (PCR)")
abline(h = 0, col = "red", lty = 2)
par(mfrow = c(1, 1))

# 10. Ajustement d'un modele PLS

pls_model <- plsr(octane ~ NIR, ncomp = 20, data = gasoline, validation = "CV")

# 11. Exploration du modele PLS

summary(pls_model)

plot(pls_model, plottype = "coef", ncomp = 1:5, legendpos = "bottomleft",
     main = "Coefficients PLS")

plot(pls_model, plottype = "validation", val.type = "RMSEP",
     main = "RMSEP en validation croisee - PLS")

validation_stats_pls <- RMSEP(pls_model, estimate = "CV")
best_ncomp_pls <- which.min(validation_stats_pls$val[1, 1, ]) - 1
print(paste("Nombre optimal de composantes PLS :", best_ncomp_pls))
