library(glmnet)

set.seed(123)

fat <- readRDS("fat.Rds")
y <- fat$brozek
X_raw <- fat[, setdiff(names(fat), "brozek")]
X <- scale(as.matrix(X_raw))

cat("1. Analyse descriptive\n")
cat("Dimensions :", nrow(fat), "lignes x", ncol(fat), "colonnes\n")
cat("Nombre de NA :", sum(is.na(fat)), "\n\n")
print(summary(fat))
cat("\nEcarts-types :\n")
print(sapply(fat, sd))

cat("\n2. Matrice des correlations\n")
Cor <- cor(fat)
print(round(Cor, 3))
cat("\nCorrelations avec brozek :\n")
print(sort(round(Cor[setdiff(rownames(Cor), "brozek"), "brozek"], 3), decreasing = TRUE))

pdf("correlations.pdf", width = 10, height = 8)
heatmap(Cor, symm = TRUE)

dev.off()
cat("Graphique des correlations exporte dans correlations.pdf\n")

cat("\n3. Regression lineaire sur donnees normalisees\n")
fat_std <- data.frame(brozek = y, X)
mod_lm <- lm(brozek ~ ., data = fat_std)
print(summary(mod_lm))

cat("\n1. Lasso avec lambda = 0.1\n")
lasso_01 <- glmnet(X, y, alpha = 1, lambda = 0.1, standardize = FALSE)
print(coef(lasso_01))
supp_lasso_01 <- setdiff(rownames(coef(lasso_01))[as.vector(coef(lasso_01) != 0)], "(Intercept)")
cat("Support Lasso (lambda = 0.1) :", paste(supp_lasso_01, collapse = ", "), "\n")
cat("Cardinal du support :", length(supp_lasso_01), "\n")

cat("\n2. Chemin de regularisation du Lasso\n")
lasso_path <- glmnet(X, y, alpha = 1, standardize = FALSE)
nz_lasso <- apply(as.matrix(lasso_path$beta) != 0, 2, sum)
idx5 <- which(nz_lasso == 5)[1]
lambda5 <- lasso_path$lambda[idx5]
supp_lasso_5 <- setdiff(
  rownames(coef(lasso_path, s = lambda5))[as.vector(coef(lasso_path, s = lambda5) != 0)],
  "(Intercept)"
)
cat("Lambda donnant un support de cardinal 5 :", lambda5, "\n")
cat("Support correspondant :", paste(supp_lasso_5, collapse = ", "), "\n")

pdf("lasso_path.pdf", width = 10, height = 8)
plot(lasso_path, xvar = "lambda", label = TRUE)
dev.off()
cat("Chemin du Lasso exporte dans lasso_path.pdf\n")

cat("\n3. Validation croisee Lasso\n")
foldid <- sample(rep(1:10, length.out = nrow(X)))
cv_lasso <- cv.glmnet(X, y, alpha = 1, standardize = FALSE, foldid = foldid)
cat("lambda.min =", cv_lasso$lambda.min, "\n")
cat("lambda.1se =", cv_lasso$lambda.1se, "\n")
cat("MSE min =", min(cv_lasso$cvm), "\n")

pdf("cv_lasso.pdf", width = 10, height = 8)
plot(cv_lasso)
dev.off()
cat("Courbe CV Lasso exportee dans cv_lasso.pdf\n")

fit_lasso_opt <- glmnet(X, y, alpha = 1, lambda = cv_lasso$lambda.min, standardize = FALSE)
supp_lasso_opt <- setdiff(rownames(coef(fit_lasso_opt))[as.vector(coef(fit_lasso_opt) != 0)], "(Intercept)")
cat("Support Lasso optimal :", paste(supp_lasso_opt, collapse = ", "), "\n")

cat("\n4. Comparaison des performances\n")
cv_mse <- function(kind, data, foldid) {
  se <- c()

  for (k in sort(unique(foldid))) {
    tr <- foldid != k
    te <- foldid == k

    xtr <- scale(as.matrix(data[tr, setdiff(names(data), "brozek")]))
    ctr <- attr(xtr, "scaled:center")
    scl <- attr(xtr, "scaled:scale")
    xte <- scale(as.matrix(data[te, setdiff(names(data), "brozek")]), center = ctr, scale = scl)
    ytr <- data$brozek[tr]
    yte <- data$brozek[te]

    if (kind == "ols_all") {
      fit <- lm(brozek ~ ., data = data[tr, ])
      pred <- predict(fit, newdata = data[te, ])
    }

    if (kind == "lasso_opt") {
      cvfit <- cv.glmnet(xtr, ytr, alpha = 1, standardize = FALSE)
      pred <- as.numeric(predict(cvfit, newx = xte, s = "lambda.min"))
    }

    if (kind == "post_lasso") {
      cvfit <- cv.glmnet(xtr, ytr, alpha = 1, standardize = FALSE)
      fit_lasso <- glmnet(xtr, ytr, alpha = 1, lambda = cvfit$lambda.min, standardize = FALSE)
      supp <- setdiff(rownames(coef(fit_lasso))[as.vector(coef(fit_lasso) != 0)], "(Intercept)")
      df_tr <- data.frame(brozek = ytr, xtr[, supp, drop = FALSE])
      df_te <- data.frame(xte[, supp, drop = FALSE])
      fit <- lm(brozek ~ ., data = df_tr)
      pred <- predict(fit, newdata = df_te)
    }

    se <- c(se, (yte - pred)^2)
  }

  mean(se)
}

mse_ols_all <- cv_mse("ols_all", fat, foldid)
mse_lasso_opt <- cv_mse("lasso_opt", fat, foldid)
mse_post_lasso <- cv_mse("post_lasso", fat, foldid)

cat("MSE CV MCO complet   =", mse_ols_all, "\n")
cat("MSE CV Lasso optimal =", mse_lasso_opt, "\n")
cat("MSE CV Post-Lasso    =", mse_post_lasso, "\n")

cat("\n5. Ridge avec lambda = 0.1\n")
ridge_01 <- glmnet(X, y, alpha = 0, lambda = 0.1, standardize = FALSE)
print(coef(ridge_01))
supp_ridge_01 <- setdiff(rownames(coef(ridge_01))[as.vector(abs(coef(ridge_01)) > 1e-10)], "(Intercept)")
cat("Support Ridge (lambda = 0.1) :", paste(supp_ridge_01, collapse = ", "), "\n")
cat("Cardinal du support :", length(supp_ridge_01), "\n")

cat("\n6. Chemin de regularisation du Ridge\n")
ridge_path <- glmnet(X, y, alpha = 0, standardize = FALSE)
nz_ridge <- apply(abs(as.matrix(ridge_path$beta)) > 1e-8, 2, sum)
cat("Taille minimale du support sur le chemin :", min(nz_ridge), "\n")
cat("Taille maximale du support sur le chemin :", max(nz_ridge), "\n")

pdf("ridge_path.pdf", width = 10, height = 8)
plot(ridge_path, xvar = "lambda", label = TRUE)
dev.off()
cat("Chemin du Ridge exporte dans ridge_path.pdf\n")

cat("\n7. Validation croisee Ridge\n")
cv_ridge <- cv.glmnet(X, y, alpha = 0, standardize = FALSE, foldid = foldid)
cat("lambda.min =", cv_ridge$lambda.min, "\n")
cat("MSE min =", min(cv_ridge$cvm), "\n")

pdf("cv_ridge.pdf", width = 10, height = 8)
plot(cv_ridge)
dev.off()
cat("Courbe CV Ridge exportee dans cv_ridge.pdf\n")

cat("\n8. Differences Lasso / Ridge\n")
cat("Le Lasso annule certains coefficients, contrairement au Ridge qui les retrecit sans les annuler en general.\n")

cat("\n9. Elastic Net avec lambda = 0.1\n")
enet_01 <- glmnet(X, y, alpha = 0.1, lambda = 0.1, standardize = FALSE)
enet_09 <- glmnet(X, y, alpha = 0.9, lambda = 0.1, standardize = FALSE)
print(coef(enet_01))
print(coef(enet_09))

supp_enet_01 <- setdiff(rownames(coef(enet_01))[as.vector(coef(enet_01) != 0)], "(Intercept)")
supp_enet_09 <- setdiff(rownames(coef(enet_09))[as.vector(coef(enet_09) != 0)], "(Intercept)")

cat("Support Elastic Net alpha = 0.1 :", paste(supp_enet_01, collapse = ", "), "\n")
cat("Cardinal :", length(supp_enet_01), "\n")
cat("Support Elastic Net alpha = 0.9 :", paste(supp_enet_09, collapse = ", "), "\n")
cat("Cardinal :", length(supp_enet_09), "\n")

cat("\n10. Couple (lambda, alpha) optimal\n")
alphas <- seq(0, 1, by = 0.05)
res <- do.call(rbind, lapply(alphas, function(a) {
  cvfit <- cv.glmnet(X, y, alpha = a, standardize = FALSE, foldid = foldid)
  data.frame(alpha = a, lambda = cvfit$lambda.min, mse = min(cvfit$cvm))
}))

res <- res[order(res$mse), ]
print(res)
cat("\nMeilleur couple : alpha =", res$alpha[1], ", lambda =", res$lambda[1], ", MSE =", res$mse[1], "\n")
