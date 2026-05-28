# ============================================================
# TP6 : Regression PLS
# ============================================================

# ------------------------------------------------------------
# 1. Installation et chargement des packages
# ------------------------------------------------------------
library(pls)
library(caret)
library(ggplot2)
library(corrplot)

# ------------------------------------------------------------
# 2. Chargement et description du jeu de donnees gasoline
# ------------------------------------------------------------
data(gasoline)

# Description du jeu de donnees :
# - gasoline$octane : variable a expliquer (indice d'octane)
# - gasoline$NIR    : spectres infrarouges (variables explicatives)
#   401 variables spectrales mesurees sur 60 echantillons d'essence

# Taille du jeu de donnees
dim(gasoline$NIR)
str(gasoline)

# Statistiques descriptives
summary(gasoline$octane)
apply(gasoline$NIR, 2, summary)

# Visualisation des spectres
plot(1:401, gasoline$NIR[1,], type = "l", ylim = range(gasoline$NIR),
     xlab = "Longueur d'onde", ylab = "Absorbance",
     main = "Spectres NIR des echantillons d'essence")
for(i in 2:nrow(gasoline$NIR)) {
  lines(1:401, gasoline$NIR[i,], col = i)
}

# Matrice de correlation (sur un sous-echantillon de longueurs d'onde pour la lisibilite)
cor_matrix <- cor(gasoline$NIR[, seq(1, 401, by = 20)])
corrplot(cor_matrix, method = "color", type = "upper",
         title = "Matrice de correlation (sous-echantillon)")

# On observe des correlations tres elevees entre les variables spectrales
# (proches de 1), ce qui indique une forte multicolinearte.
# Ce phénomène est typique des données spectrales et justifie l'utilisation
# de methodes comme PCR ou PLS.

# ------------------------------------------------------------
# 3. Regression lineaire multiple
# ------------------------------------------------------------
# Conversion en data.frame
X <- as.data.frame(unclass(gasoline$NIR))
colnames(X) <- paste0("X", 1:ncol(X))
df <- data.frame(octane = gasoline$octane, X)

# Ajustement du modele LM
lm_model <- lm(octane ~ ., data = df)
summary(lm_model)

# Commentaires :
# - Le modele LM avec 401 variables et 60 observations est surajuste.
# - Le nombre de variables excede largement le nombre d'observations.
# - Les p-values ne sont pas fiables car les coefficients ne sont pas identifiables.
# - La multicolinearte extreme rend le modele instable et non interpretable.
# - La regression lineaire classique n'est pas adaptee ici.

# ------------------------------------------------------------
# 4. ACP sur les variables explicatives
# ------------------------------------------------------------
pca_model <- prcomp(gasoline$NIR, center = TRUE, scale. = FALSE)

# ------------------------------------------------------------
# 5. Analyse de l'ACP et nombre d'axes a retenir
# ------------------------------------------------------------
# Variance expliquee
summary(pca_model)

# Scree plot
plot(pca_model, type = "l", main = "Scree Plot - ACP")

# Cumul de variance expliquee
var_explained <- cumsum(pca_model$sdev^2) / sum(pca_model$sdev^2)
plot(var_explained, type = "b", pch = 19,
     xlab = "Nombre de composantes", ylab = "Variance expliquee cumulee",
     main = "Variance expliquee cumulee par l'ACP")
abline(h = 0.95, col = "red", lty = 2)
abline(h = 0.99, col = "blue", lty = 2)
legend("bottomright", legend = c("95%", "99%"), col = c("red", "blue"), lty = 2)

# On observe que les premieres composantes captent l'essentiel de l'information.
# Par exemple, environ 95% de la variance est expliquee par moins de 10 composantes.
# Le nombre d'axes utiles a retenir est donc limite (environ 5 a 10).

# ------------------------------------------------------------
# 6. Ajustement d'un modele PCR
# ------------------------------------------------------------
pcr_model <- pcr(octane ~ NIR, ncomp = 20, data = gasoline, validation = "CV")

# ------------------------------------------------------------
# 7. Exploration du modele PCR
# ------------------------------------------------------------
summary(pcr_model)

# Coefficients des variables pour differents nombres de composantes
# (non directement interpretables car dans l'espace des composantes)
plot(pcr_model, plottype = "coef", ncomp = 1:5, legendpos = "bottomleft",
     main = "Coefficients PCR")

# Impact du nombre de composantes sur les indicateurs de qualite
plot(pcr_model, plottype = "validation", val.type = "RMSEP",
     main = "RMSEP en validation croisee - PCR")

# Nombre optimal de composantes (minimum RMSE CV)
validation_stats <- RMSEP(pcr_model, estimate = "CV")
best_ncomp_pcr <- which.min(validation_stats$val[1, 1, ]) - 1  # -1 car intercept en position 1
print(paste("Nombre optimal de composantes PCR :", best_ncomp_pcr))

# ------------------------------------------------------------
# 8. MSE du modele PCR
# ------------------------------------------------------------
# Predictions avec le nombre optimal de composantes
pred_pcr <- predict(pcr_model, ncomp = best_ncomp_pcr, newdata = gasoline)
mse_pcr <- mean((gasoline$octane - pred_pcr)^2)
print(paste("MSE PCR (apprentissage, ncomp =", best_ncomp_pcr, ") :", round(mse_pcr, 4)))

# MSE en validation croisee
# On peut egalement calculer le MSEP moyen en CV
msep_pcr <- validation_stats$val[1, 1, best_ncomp_pcr + 1]^2
print(paste("MSEP PCR (validation croisee, ncomp =", best_ncomp_pcr, ") :", round(msep_pcr, 4)))

# ------------------------------------------------------------
# 9. Residus du modele PCR
# ------------------------------------------------------------
resid_pcr <- gasoline$octane - pred_pcr

par(mfrow = c(2, 2))
# Residus vs valeurs predites
plot(pred_pcr, resid_pcr, pch = 19, col = "blue",
     xlab = "Valeurs predites", ylab = "Residus",
     main = "Residus vs Valeurs predites (PCR)")
abline(h = 0, col = "red", lty = 2)

# QQ-plot des residus
qqnorm(resid_pcr, main = "QQ-plot des residus (PCR)")
qqline(resid_pcr, col = "red")

# Histogramme des residus
hist(resid_pcr, breaks = 15, col = "lightblue", border = "white",
     main = "Distribution des residus (PCR)", xlab = "Residus")

# Residus vs index
plot(1:length(resid_pcr), resid_pcr, type = "b", pch = 19, col = "blue",
     xlab = "Index", ylab = "Residus",
     main = "Residus vs Index (PCR)")
abline(h = 0, col = "red", lty = 2)
par(mfrow = c(1, 1))

# Analyse :
# - Les residus semblent centres autour de zero et relativement homogenes.
# - Le QQ-plot permet de verifier la normalite (approximative ici vu le faible n).
# - Pas de pattern evident dans les residus vs predits, ce qui est rassurant.

# ------------------------------------------------------------
# 10. Ajustement d'un modele PLS
# ------------------------------------------------------------
pls_model <- plsr(octane ~ NIR, ncomp = 20, data = gasoline, validation = "CV")

# ------------------------------------------------------------
# 11. Exploration du modele PLS
# ------------------------------------------------------------
summary(pls_model)

# Coefficients des variables
plot(pls_model, plottype = "coef", ncomp = 1:5, legendpos = "bottomleft",
     main = "Coefficients PLS")

# Impact du nombre de composantes sur les indicateurs de qualite
plot(pls_model, plottype = "validation", val.type = "RMSEP",
     main = "RMSEP en validation croisee - PLS")

# Nombre optimal de composantes (minimum RMSE CV)
validation_stats_pls <- RMSEP(pls_model, estimate = "CV")
best_ncomp_pls <- which.min(validation_stats_pls$val[1, 1, ]) - 1
print(paste("Nombre optimal de composantes PLS :", best_ncomp_pls))

# ------------------------------------------------------------
# 12. MSE du modele PLS
# ------------------------------------------------------------
pred_pls <- predict(pls_model, ncomp = best_ncomp_pls, newdata = gasoline)
mse_pls <- mean((gasoline$octane - pred_pls)^2)
print(paste("MSE PLS (apprentissage, ncomp =", best_ncomp_pls, ") :", round(mse_pls, 4)))

# MSEP en validation croisee
msep_pls <- validation_stats_pls$val[1, 1, best_ncomp_pls + 1]^2
print(paste("MSEP PLS (validation croisee, ncomp =", best_ncomp_pls, ") :", round(msep_pls, 4)))

# ------------------------------------------------------------
# 13. Residus du modele PLS
# ------------------------------------------------------------
resid_pls <- gasoline$octane - pred_pls

par(mfrow = c(2, 2))
# Residus vs valeurs predites
plot(pred_pls, resid_pls, pch = 19, col = "darkgreen",
     xlab = "Valeurs predites", ylab = "Residus",
     main = "Residus vs Valeurs predites (PLS)")
abline(h = 0, col = "red", lty = 2)

# QQ-plot des residus
qqnorm(resid_pls, main = "QQ-plot des residus (PLS)")
qqline(resid_pls, col = "red")

# Histogramme des residus
hist(resid_pls, breaks = 15, col = "lightgreen", border = "white",
     main = "Distribution des residus (PLS)", xlab = "Residus")

# Residus vs index
plot(1:length(resid_pls), resid_pls, type = "b", pch = 19, col = "darkgreen",
     xlab = "Index", ylab = "Residus",
     main = "Residus vs Index (PLS)")
abline(h = 0, col = "red", lty = 2)
par(mfrow = c(1, 1))

# ------------------------------------------------------------
# 14. Comparaison PCR vs PLS
# ------------------------------------------------------------

# Comparaison des RMSEP
par(mfrow = c(1, 2))
plot(pcr_model, plottype = "validation", val.type = "RMSEP",
     main = "PCR - RMSEP CV")
plot(pls_model, plottype = "validation", val.type = "RMSEP",
     main = "PLS - RMSEP CV")
par(mfrow = c(1, 1))

# Comparaison directe des predictions
plot(gasoline$octane, pred_pcr, pch = 19, col = "blue",
     xlab = "Octane observe", ylab = "Octane predit",
     main = "PCR vs PLS : Predictions")
points(gasoline$octane, pred_pls, pch = 19, col = "darkgreen")
abline(0, 1, col = "red", lty = 2)
legend("topleft", legend = c("PCR", "PLS"), col = c("blue", "darkgreen"), pch = 19)

# Comparaison des MSE/MSEP
print(paste("MSEP PCR (CV) :", round(msep_pcr, 4)))
print(paste("MSEP PLS (CV) :", round(msep_pls, 4)))

# Conclusion :
# - PLS utilise les composantes latentes qui maximisent la covariance avec Y,
#   tandis que PCR maximise seulement la variance de X.
# - PLS atteint generalement un meilleur pouvoir predictif avec moins de composantes.
# - Le nombre optimal de composantes est souvent plus faible pour PLS que pour PCR.
# - PLS semble donc mieux adapte a ce probleme de prediction.

# ------------------------------------------------------------
# 15. Autres methodes possibles
# ------------------------------------------------------------

# Ridge Regression :
# - Penalise la norme L2 des coefficients.
# - Gere la multicolinearte en reduisant la variance des estimateurs.
# - Conserve toutes les variables mais les regularise.
# - Contrairement a PCR/PLS qui font de la reduction de dimension,
#   Ridge garde l'interpretation sur les variables initiales (deformee).

# Lasso Regression :
# - Penalise la norme L1 des coefficients.
# - Effectue une selection de variables (certains coefficients deviennent nuls).
# - Utile quand on suspecte que peu de variables sont reellement pertinentes.
# - Sur ces donnees spectrales, Ridge ou Elastic Net seraient souvent preferes.

# Elastic Net :
# - Combinaison de Ridge et Lasso.
# - Gere la multicolinearte tout en permettant la selection de variables.

# Differences attendues :
# - Ridge/Lasso/Elastic Net ne construisent pas de composantes latentes.
# - Les resultats seraient interpretables en termes de longueurs d'onde (variables).
# - Les performances predictives seraient comparables, mais la structure
#   du modele serait differente.
# - Avec 401 variables fortement correlees, Ridge serait particulierement adapte.

# ============================================================
# Fin du script
# ============================================================
