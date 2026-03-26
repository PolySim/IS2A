# 1

knitr::opts_chunk$set(echo = TRUE, warning = FALSE, message = FALSE)

# 1.1
data_path <- "data.csv"

wine <- read.csv(data_path, sep = ",", dec = ".")
wine$quality <- factor(wine$quality)

# 1.2
quant_vars <- setdiff(names(wine), "quality")
x <- wine[, quant_vars]

cat("\n1) ANALYSE DESCRIPTIVE\n")
cat(sprintf("Nombre d'observations : %d\n", nrow(wine)))
cat(sprintf("Nombre de variables    : %d\n", ncol(wine)))

cat("\nResume statistique :\n")
print(knitr::kable(summary(wine)))

cat("\nValeurs manquantes ?\n")
print(knitr::kable(colSums(is.na(wine)) > 0))
cat("\nRepartition de la qualite :\n")
print(knitr::kable(table(wine$quality)))
cat("\nMoyennes par niveau de qualite :\n")
print(knitr::kable(aggregate(x, by = list(quality = wine$quality), FUN = mean)))

# 1.3
png("01_boxplots_quality.png", width = 1800, height = 1200, res = 150)
par(mfrow = c(3, 4), mar = c(7, 4, 3, 1))
for (v in quant_vars) {
  boxplot(
    wine[[v]] ~ wine$quality,
    main = v,
    xlab = "Quality",
    ylab = v,
    las = 2,
    col = "lightgray",
    border = "gray30"
  )
}
dev.off()

numeric_quality <- as.numeric(as.character(wine$quality))
png("02_correlations_quality.png", width = 1600, height = 900, res = 150)
barplot(
  sort(cor(x, numeric_quality), decreasing = TRUE),
  las = 2,
  col = "steelblue",
  main = "Correlations avec la qualite numerique",
  ylab = "Correlation"
)
abline(h = 0, lty = 2)
dev.off()

# 2

library(MASS)

# 2.1

cat("\n2) ANALYSE FACTORIELLE DISCRIMINANTE (AFD)\n")
cat("Avec MASS::lda, sert a la fois a l'AFD et a l'ADL.
  Pour iris, cela ne change pas l'ajustement du modele,
  seule l'exploitation differe :
  visualisation factorielle ou prediction de classe.\n")

# 2.2
afd_fit <- lda(quality ~ ., data = wine)
cat("\nModele lda ajuste :\n")
print(afd_fit)

# 2.3
tbc <- function(truth, pred) {
  ok <- !is.na(pred)
  mean(truth[ok] == pred[ok])
}
tmc <- function(truth, pred) {
  1 - tbc(truth, pred)
}

evaluate_rule <- function(truth, pred, label) {
  cm <- table(Reelle = truth, Predite = pred)
  cat("\n\n", label, "\n")
  print(knitr::kable(cm))
  cat(sprintf("TBC : %.4f\n", tbc(truth, pred)))
  cat(sprintf("TMC : %.4f\n", tmc(truth, pred)))
  invisible(list(
    confusion = cm,
    tbc = tbc(truth, pred), tmc = tmc(truth, pred)
  ))
}

afd_pred <- predict(afd_fit)
evaluate_rule(
  wine$quality,
  afd_pred$class,
  "Predictions sur l'echantillon complet (AFD/lda)"
)

# 2.4

scores <- as.matrix(x) %*% afd_fit$scaling
scores_df <- data.frame(scores, quality = wine$quality)

png("03_plan_factoriel_afd.png", width = 1400, height = 1000, res = 150)
plot(
  scores_df$LD1,
  scores_df$LD2,
  col = as.integer(scores_df$quality),
  pch = 19,
  xlab = "LD1",
  ylab = "LD2",
  main = "Premier plan factoriel discriminant"
)
legend(
  "topright",
  legend = levels(scores_df$quality),
  col = seq_along(levels(scores_df$quality)),
  pch = 19,
  title = "Quality"
)
dev.off()

# 3

# 3.1

cat("\n3) EVALUATION DES REGLES DE CLASSEMENT\n")
cat("Le taux calcule sur l'echantillon ayant servi a
  l'apprentissage est optimiste, car le modele
  est evalue sur les memes donnees que celles utilisees
  pour estimer les directions discriminantes.\n")

# 3.4
set.seed(123)
n <- nrow(wine)
idx_train <- sample(seq_len(n), size = floor(0.7 * n))
wine_train <- wine[idx_train, , drop = FALSE]
wine_test <- wine[-idx_train, , drop = FALSE]

afd_train_fit <- lda(quality ~ ., data = wine_train)
pred_train <- predict(afd_train_fit, newdata = wine_train)$class
pred_test <- predict(afd_train_fit, newdata = wine_test)$class

train_eval <- evaluate_rule(
  wine_train$quality,
  pred_train,
  "Performance sur l'echantillon d'apprentissage (70%)"
)

test_eval <- evaluate_rule(
  wine_test$quality,
  pred_test,
  "Performance sur l'echantillon test (30%)"
)

# 3.6
kfold_cv <- function(formula, data, k, method = c("lda", "qda")) {
  method <- match.arg(method)

  n <- nrow(data)
  folds <- sample(rep(1:k, length.out = n))
  pred <- rep(NA_character_, n)
  classes <- levels(data[[all.vars(formula)[1]]])

  for (fold in seq_len(k)) {
    train <- data[folds != fold, , drop = FALSE]
    test <- data[folds == fold, , drop = FALSE]

    fit <- if (method == "lda") {
      lda(formula, data = train)
    } else {
      qda(formula, data = train)
    }

    pred[folds == fold] <- as.character(predict(fit, newdata = test)$class)
  }

  pred <- factor(pred, levels = classes)
  truth <- data[[all.vars(formula)[1]]]
  list(
    pred = pred,
    confusion = table(Reelle = truth, Predite = pred),
    tbc = tbc(truth, pred),
    tmc = tmc(truth, pred)
  )
}

cv5_afd <- kfold_cv(quality ~ ., data = wine, k = 5, method = "lda")
cat("\nValidation croisee 5-fold (lda / AFD)\n")
print(cv5_afd$confusion)
cat(sprintf("TBC CV : %.4f\n", cv5_afd$tbc))
cat(sprintf("TMC CV : %.4f\n", cv5_afd$tmc))

# 4

# 4.1
cat("\n4) ANALYSE DISCRIMINANTE PROBABILISTE\n")
adl_fit <- lda(quality ~ ., data = wine)
adl_pred <- predict(adl_fit)$class
adl_eval <- evaluate_rule(
  wine$quality,
  adl_pred,
  "ADL via lda sur l'echantillon complet"
)

# 4.2
adl_loo <- lda(quality ~ ., data = wine, CV = TRUE)
evaluate_rule(
  wine$quality,
  adl_loo$class,
  "ADL via lda avec validation croisee leave-one-out"
)

# 4.3
qda_fit <- qda(quality ~ ., data = wine)
qda_pred <- predict(qda_fit)$class
evaluate_rule(
  wine$quality,
  qda_pred,
  "ADQ via qda sur l'echantillon complet"
)

# 4.4
qda_loo <- qda(quality ~ ., data = wine, CV = TRUE)
evaluate_rule(
  wine$quality,
  qda_loo$class,
  "ADQ via qda avec validation croisee leave-one-out"
)
