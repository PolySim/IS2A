# TP3 - Classification Supervisee
# Classification bayesienne naive avec loi normale multivariee

library(mvtnorm)
library(ggplot2)
library(ROCR)

set.seed(42)

# --- Question 2 : Simulation de Y ~ B(1, 0.5) ---
n <- 1000
Y <- rbinom(n, size = 1, prob = 0.5)

# --- Question 3 : Simulation de X sachant Y ---
# Parametres classe 0
mu0 <- c(0, 0)
sigma0 <- matrix(c(1, 0, 0, 1), nrow = 2)

# Parametres classe 1
mu1 <- c(2, 2)
sigma1 <- matrix(c(1, 0.8, 0.8, 1), nrow = 2)

X <- matrix(0, nrow = n, ncol = 2)
X[Y == 0, ] <- rmvnorm(sum(Y == 0), mean = mu0, sigma = sigma0)
X[Y == 1, ] <- rmvnorm(sum(Y == 1), mean = mu1, sigma = sigma1)
colnames(X) <- c("X1", "X2")

# --- Question 4 : Representation graphique ---
data_plot <- data.frame(X, Y = as.factor(Y))
p1 <- ggplot(data_plot, aes(x = X1, y = X2, color = Y)) +
  geom_point(alpha = 0.6) +
  labs(title = "Donnees simulees") +
  theme_minimal()
print(p1)

# --- Question 5 : Calcul des f_i(x) pour x = (1, 2) ---
x_test <- c(1, 2)
f0 <- dmvnorm(x_test, mean = mu0, sigma = sigma0)
f1 <- dmvnorm(x_test, mean = mu1, sigma = sigma1)
cat("f0(x) =", f0, "\n")
cat("f1(x) =", f1, "\n")

# --- Question 6 : Probabilites d'appartenance ---
pi0 <- mean(Y == 0)
pi1 <- mean(Y == 1)

p0 <- pi0 * f0 / (pi0 * f0 + pi1 * f1)
p1_x <- pi1 * f1 / (pi0 * f0 + pi1 * f1)
cat("P(Y=0|X=x) =", p0, "\n")
cat("P(Y=1|X=x) =", p1_x, "\n")

# --- Question 7 : Regle du MAP ---
classe_predite <- ifelse(p1_x > p0, 1, 0)
cat("Classe predite pour x=(1,2) :", classe_predite, "\n")

# --- Question 8 : Classification de tous les individus ---
f0_all <- dmvnorm(X, mean = mu0, sigma = sigma0)
f1_all <- dmvnorm(X, mean = mu1, sigma = sigma1)

t0_all <- pi0 * f0_all / (pi0 * f0_all + pi1 * f1_all)
t1_all <- pi1 * f1_all / (pi0 * f0_all + pi1 * f1_all)
t_all <- cbind(t0_all, t1_all)

Yp <- ifelse(t1_all > 0.5, 1, 0)

# --- Question 9 : Representation avec classes predites ---
data_ggplot <- data.frame(X, Y = as.factor(Y), YPred = as.factor(Yp))
data_ggplot$Classement <- factor(2 * as.numeric(Y) + Yp,
  levels = c(0, 1, 2, 3),
  labels = c(
    "Y = 0, Ypred = 0", "Y = 0, Ypred = 1",
    "Y = 1, Ypred = 0", "Y = 1, Ypred = 1"
  )
)

p2 <- ggplot(data = data_ggplot, aes(x = X1, y = X2, color = Classement, shape = Classement)) +
  geom_point(size = 2, alpha = 0.8) +
  labs(title = "Donnees simulees - Erreurs de classement") +
  theme_minimal() +
  scale_colour_brewer(palette = "Set1")
print(p2)

# --- Question 10 : Matrice de confusion et taux de mauvais classement ---
confusion <- table(Reel = Y, Predit = Yp)
cat("\nMatrice de confusion :\n")
print(confusion)

taux_erreur <- mean(Y != Yp)
cat("Taux de mauvais classement :", taux_erreur, "\n")

# --- Question 11 : Specificite et sensibilite ---
# Sensibilite = P(Yp=1 | Y=1) = VP / (VP + FN)
# Specificite = P(Yp=0 | Y=0) = VN / (VN + FP)
sensibilite <- confusion[2, 2] / sum(confusion[2, ])
specificite <- confusion[1, 1] / sum(confusion[1, ])
cat("Sensibilite :", sensibilite, "\n")
cat("Specificite :", specificite, "\n")

# --- Question 12 : Courbe ROC et AUC ---
pred_ROC <- prediction(t_all[, 2], Y)
perf_ROC <- performance(pred_ROC, "tpr", "fpr")

AUC_ROC <- performance(pred_ROC, "auc")

data_roc <- data.frame(X = perf_ROC@x.values[[1]], Y = perf_ROC@y.values[[1]])
p3 <- ggplot(data = data_roc, aes(x = X, y = Y)) +
  geom_line(size = 1) +
  labs(
    title = "Courbe ROC",
    subtitle = paste0("AUC : ", round(AUC_ROC@y.values[[1]], 4))
  ) +
  ylab(perf_ROC@y.name) +
  xlab(perf_ROC@x.name) +
  theme_minimal() +
  coord_fixed()
print(p3)

# --- Question 13 : Seuil optimal ---
CutPoint <- performance(pred_ROC, "cost", cost.fp = 1, cost.fn = 1)
SeuilOptimal <- pred_ROC@cutoffs[[1]][which.min(CutPoint@y.values[[1]])]
cat("Seuil optimal :", SeuilOptimal, "\n")

# Courbe ROC avec seuil optimal
idx_opt <- which.min(CutPoint@y.values[[1]])
p4 <- ggplot(data = data_roc, aes(x = X, y = Y)) +
  geom_line(size = 1) +
  geom_point(
    aes(
      x = perf_ROC@x.values[[1]][idx_opt],
      y = perf_ROC@y.values[[1]][idx_opt]
    ),
    colour = "red", size = 3
  ) +
  labs(
    title = "Courbe ROC",
    subtitle = paste0("AUC : ", round(AUC_ROC@y.values[[1]], 4))
  ) +
  ylab(perf_ROC@y.name) +
  xlab(perf_ROC@x.name) +
  annotate(
    geom = "text",
    x = 1.1 * perf_ROC@x.values[[1]][idx_opt],
    y = 0.99 * perf_ROC@y.values[[1]][idx_opt],
    label = "Couple (Se, 1-Sp) associe au seuil optimal",
    color = "black", hjust = 0
  ) +
  theme_minimal() +
  coord_fixed()
print(p4)

# --- Question 14 : Classification avec seuil optimal ---
Yp_opt <- ifelse(t_all[, 2] > SeuilOptimal, 1, 0)

confusion_opt <- table(Reel = Y, Predit = Yp_opt)
cat("\nMatrice de confusion (seuil optimal) :\n")
print(confusion_opt)

taux_erreur_opt <- mean(Y != Yp_opt)
cat("Taux de mauvais classement (seuil optimal) :", taux_erreur_opt, "\n")
