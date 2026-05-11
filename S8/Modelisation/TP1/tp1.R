required_packages <- c("missMDA", "mice")

set.seed(123)

data(mtcars)
df_complete <- mtcars[, 1:7]
chosen_vars <- c("disp", "hp", "wt")
n <- nrow(df_complete)
n_miss <- round(0.2 * n)
m <- 5

cat("1) Analyse descriptive rapide\n")
print(str(df_complete))
print(summary(df_complete))
print(round(cor(df_complete), 3))

round_numeric_df <- function(x, digits = 3) {
  is_num <- vapply(x, is.numeric, logical(1))
  x[is_num] <- lapply(x[is_num], round, digits = digits)
  x
}

plot_missing_matrix <- function(data, title) {
  miss <- is.na(data)
  image(
    t(miss[nrow(miss):1, ]),
    col = c("grey90", "firebrick"),
    axes = FALSE,
    main = title
  )
  axis(1, at = seq(0, 1, length.out = ncol(data)), labels = colnames(data), las = 2)
  axis(2, at = seq(0, 1, length.out = nrow(data)), labels = rev(seq_len(nrow(data))), las = 2, cex.axis = 0.6)
  box()
}

plot_distribution_comparison <- function(data_with_na, full_data, target_var, reference_var) {
  missing_indicator <- is.na(data_with_na[[target_var]])

  if (all(!missing_indicator) || all(missing_indicator)) {
    plot.new()
    title(main = sprintf("%s : comparaison impossible", target_var))
    return(invisible(NULL))
  }

  observed <- full_data[[reference_var]][!missing_indicator]
  missing <- full_data[[reference_var]][missing_indicator]

  dens_obs <- density(observed)
  dens_mis <- density(missing)

  plot(
    dens_obs,
    col = "steelblue",
    lwd = 2,
    main = sprintf("%s manquant vs observe via %s", target_var, reference_var),
    xlab = reference_var
  )
  lines(dens_mis, col = "darkorange", lwd = 2)
  legend(
    "topright",
    legend = c("ligne observee", "ligne avec valeur manquante"),
    col = c("steelblue", "darkorange"),
    lwd = 2,
    bty = "n"
  )
}

inject_mcar <- function(data, vars, n_miss) {
  out <- data
  for (var in vars) {
    idx <- sample(seq_len(nrow(out)), size = n_miss)
    out[idx, var] <- NA
  }
  out
}

inject_mar <- function(data, vars, n_miss) {
  out <- data

  scores <- list(
    disp = scale(data$wt + data$cyl),
    hp = scale(data$disp + data$wt),
    wt = scale(data$hp + data$qsec)
  )

  for (var in vars) {
    ord <- order(scores[[var]][, 1], decreasing = TRUE)
    out[ord[seq_len(n_miss)], var] <- NA
  }

  out
}

inject_mnar <- function(data, vars, n_miss) {
  out <- data
  for (var in vars) {
    ord <- order(data[[var]], decreasing = TRUE)
    out[ord[seq_len(n_miss)], var] <- NA
  }
  out
}

datasets_na <- list(
  MCAR = inject_mcar(df_complete, chosen_vars, n_miss),
  MAR = inject_mar(df_complete, chosen_vars, n_miss),
  MNAR = inject_mnar(df_complete, chosen_vars, n_miss)
)

missing_rate <- function(data, vars) {
  round(colMeans(is.na(data[, vars])) * 100, 1)
}

cat("\n2) Taux de donnees manquantes (%)\n")
for (name in names(datasets_na)) {
  cat("\n", name, "\n", sep = "")
  print(missing_rate(datasets_na[[name]], chosen_vars))
}

cat("\n3) Visualisation et indices des mecanismes\n")
op <- par(no.readonly = TRUE)
for (name in names(datasets_na)) {
  current <- datasets_na[[name]]

  par(mfrow = c(2, 2), mar = c(4, 4, 3, 1))
  plot_missing_matrix(current, sprintf("Matrice des NA - %s", name))
  plot_distribution_comparison(current, df_complete, "disp", "wt")
  plot_distribution_comparison(current, df_complete, "hp", "disp")
  plot_distribution_comparison(current, df_complete, "wt", "hp")

  cat("\n", name, "\n", sep = "")
  print(mice::md.pattern(current, plot = FALSE))

  for (var in chosen_vars) {
    indicator <- is.na(current[[var]])
    stats <- sapply(setdiff(names(df_complete), var), function(other) {
      mean(df_complete[[other]][indicator]) - mean(df_complete[[other]][!indicator])
    })
    cat(sprintf("Ecart de moyenne (lignes NA sur %s - lignes observees) :\n", var))
    print(round(stats, 3))
  }

  if (name == "MCAR") {
    cat("Indice attendu : pas de structure nette entre la presence de NA et les autres variables.\n")
  } else if (name == "MAR") {
    cat("Indice attendu : les NA dependent d'autres variables observees, donc des ecarts apparaissent sur ces variables.\n")
  } else {
    cat("Indice attendu : les NA touchent surtout les grandes valeurs de la variable elle-meme, ce qui deforme la distribution observee.\n")
  }
}
par(op)

compute_imputation_impact <- function(original, with_na, imputed, vars) {
  out <- data.frame(
    variable = vars,
    mean_complete = NA_real_,
    mean_observed = NA_real_,
    mean_imputed = NA_real_,
    sd_complete = NA_real_,
    sd_imputed = NA_real_,
    norm_diff = NA_real_
  )

  for (i in seq_along(vars)) {
    var <- vars[i]
    miss_idx <- is.na(with_na[[var]])
    out$mean_complete[i] <- mean(original[[var]])
    out$mean_observed[i] <- mean(with_na[[var]], na.rm = TRUE)
    out$mean_imputed[i] <- mean(imputed[[var]])
    out$sd_complete[i] <- sd(original[[var]])
    out$sd_imputed[i] <- sd(imputed[[var]])
    out$norm_diff[i] <- sqrt(sum((imputed[[var]][miss_idx] - original[[var]][miss_idx])^2))
  }

  out
}

cat("\n4) Imputation simple par PCA (missMDA::imputePCA)\n")
pca_imputed <- list()
pca_impacts <- list()

for (name in names(datasets_na)) {
  imputed_obj <- missMDA::imputePCA(datasets_na[[name]], ncp = 2)
  pca_imputed[[name]] <- as.data.frame(imputed_obj$completeObs)
  pca_impacts[[name]] <- compute_imputation_impact(df_complete, datasets_na[[name]], pca_imputed[[name]], chosen_vars)

  cat("\n", name, "\n", sep = "")
  print(round_numeric_df(pca_impacts[[name]], 3))

  par(mfrow = c(1, 3), mar = c(4, 4, 3, 1))
  for (var in chosen_vars) {
    rng <- range(c(df_complete[[var]], pca_imputed[[name]][[var]]))
    plot(
      density(df_complete[[var]]),
      col = "black",
      lwd = 2,
      main = sprintf("%s - %s", name, var),
      xlab = var,
      xlim = rng
    )
    lines(density(pca_imputed[[name]][[var]]), col = "forestgreen", lwd = 2)
    legend(
      "topright",
      legend = c("complet", "impute PCA"),
      col = c("black", "forestgreen"),
      lwd = 2,
      bty = "n"
    )
  }
}
par(op)

fit_model <- function(data) {
  lm(mpg ~ cyl + disp + hp + drat + wt + qsec, data = data)
}

extract_model_summary <- function(model) {
  s <- summary(model)
  data.frame(
    term = rownames(s$coefficients),
    estimate = s$coefficients[, "Estimate"],
    std_error = s$coefficients[, "Std. Error"],
    variance = s$coefficients[, "Std. Error"]^2,
    row.names = NULL
  )
}

complete_model <- fit_model(df_complete)
complete_summary <- extract_model_summary(complete_model)

cat("\n5) Regression sur donnees completes\n")
print(round_numeric_df(complete_summary, 4))

cat("\n5) Regression sur jeux imputes par PCA\n")
pca_models <- list()
pca_summaries <- list()

for (name in names(pca_imputed)) {
  pca_models[[name]] <- fit_model(pca_imputed[[name]])
  pca_summaries[[name]] <- extract_model_summary(pca_models[[name]])

  comparison <- merge(
    complete_summary,
    pca_summaries[[name]],
    by = "term",
    suffixes = c("_complete", paste0("_", name))
  )
  comparison$variance_ratio <- comparison[[paste0("variance_", name)]] / comparison$variance_complete

  cat("\n", name, "\n", sep = "")
  print(round_numeric_df(comparison, 4))
}

cat("\n6) Imputation multiple avec mice\n")
cat(sprintf("Nombre de jeux de donnees imputes generes : %d\n", m))

mice_objects <- list()
for (name in names(datasets_na)) {
  mice_objects[[name]] <- mice::mice(datasets_na[[name]], m = m, printFlag = FALSE, seed = 123)
  cat("\n", name, "\n", sep = "")
  print(mice_objects[[name]])
}

cat("\n7) Densityplot : valeurs observees vs imputees\n")
for (name in names(mice_objects)) {
  for (var in chosen_vars) {
    print(
      mice::densityplot(
        mice_objects[[name]],
        as.formula(paste("~", var)),
        main = sprintf("Densityplot mice - %s - %s", name, var)
      )
    )

    observed <- datasets_na[[name]][[var]][!is.na(datasets_na[[name]][[var]])]
    completed_long <- mice::complete(mice_objects[[name]], action = "long", include = FALSE)
    imputed_values <- completed_long[[var]][rep(is.na(datasets_na[[name]][[var]]), m)]

    plot(
      density(df_complete[[var]]),
      col = "black",
      lwd = 2,
      main = sprintf("Complet / observe / impute - %s - %s", name, var),
      xlab = var
    )
    lines(density(observed), col = "steelblue", lwd = 2)
    lines(density(imputed_values), col = "darkorange", lwd = 2)
    legend(
      "topright",
      legend = c("complet", "observe", "impute"),
      col = c("black", "steelblue", "darkorange"),
      lwd = 2,
      bty = "n"
    )
  }

  cat(sprintf(
    "%s : si la densite imputee s'ecarte fortement des observees/completes, l'imputation reconstruit mal la distribution initiale. Le cas MNAR doit generalement etre le plus difficile.\n",
    name
  ))
}

cat("\n8) Regression sur jeux imputes par mice + pool\n")
pooled_results <- list()

for (name in names(mice_objects)) {
  fit_mira <- with(mice_objects[[name]], lm(mpg ~ cyl + disp + hp + drat + wt + qsec))
  pooled <- mice::pool(fit_mira)
  pooled_summary <- summary(pooled)
  pooled_results[[name]] <- pooled_summary

  cat("\n", name, "\n", sep = "")
  print(round_numeric_df(pooled_summary, 4))

  variance_table <- pooled$pooled[, c("term", "ubar", "b", "t")]
  names(variance_table) <- c("term", "variance_intra", "variance_inter", "variance_totale")
  print(round_numeric_df(variance_table, 4))
}

cat("\n9) Comparaison finale aux donnees completes\n")
for (name in names(pooled_results)) {
  pooled_summary <- pooled_results[[name]][, c("term", "estimate", "std.error")]
  pooled_summary$variance_pool <- pooled_summary$std.error^2

  complete_summary_cmp <- complete_summary
  names(complete_summary_cmp) <- c("term", "estimate_complete", "std_error_complete", "variance_complete")

  pooled_summary_cmp <- pooled_summary
  names(pooled_summary_cmp) <- c("term", paste0("estimate_", name), paste0("std_error_", name), "variance_pool")

  comparison <- merge(
    complete_summary_cmp,
    pooled_summary_cmp,
    by = "term"
  )
  comparison$coef_gap <- comparison[[paste0("estimate_", name)]] - comparison$estimate_complete
  comparison$variance_ratio <- comparison$variance_pool / comparison$variance_complete

  cat("\n", name, "\n", sep = "")
  print(round_numeric_df(comparison, 4))
  cat("Lecture : une variance pool plus grande traduit l'incertitude ajoutee par les donnees manquantes et par l'imputation multiple.\n")
}
