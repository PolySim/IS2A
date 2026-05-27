suppressPackageStartupMessages({
  library(lme4)
  library(nlme)
  library(ggplot2)
  library(dplyr)
  library(emmeans)
})

cake <- readRDS("cake.Rds") |>
  mutate(
    replicate = factor(replicate),
    recipe = factor(recipe),
    temp_c = temp - mean(temp)
  )

cat("TP4 - Modeles lineaires mixtes\n")

cat("1) Analyse descriptive\n")

cat("Dimensions :", nrow(cake), "lignes et", ncol(cake), "colonnes\n")
cat("Variables :", paste(names(cake), collapse = ", "), "\n\n")
print(summary(cake))
cat("\nTableau recipe x temperature\n")
print(with(cake, table(recipe, temp)))
cat("\nMoyennes par recette\n")
print(
  cake |>
    group_by(recipe) |>
    summarise(
      moyenne_angle = mean(angle),
      ecart_type = sd(angle)
    )
)
cat("\nMoyennes par temperature\n")
print(
  cake |>
    group_by(temp) |>
    summarise(
      moyenne_angle = mean(angle),
      ecart_type = sd(angle)
    )
)

pdf("tp4_graphiques.pdf", width = 10, height = 7)

print(
  ggplot(cake, aes(x = temp, y = angle, color = recipe)) +
    geom_point(alpha = 0.7) +
    geom_smooth(method = "lm", se = FALSE) +
    labs(
      title = "Angle en fonction de la temperature",
      x = "Temperature (deg F)",
      y = "Angle de rupture"
    ) +
    theme_minimal()
)

print(
  ggplot(cake, aes(x = recipe, y = angle, fill = recipe)) +
    geom_boxplot(alpha = 0.8) +
    labs(
      title = "Distribution de l'angle par recette",
      x = "Recette",
      y = "Angle de rupture"
    ) +
    theme_minimal()
)

print(
  ggplot(cake, aes(x = temp, y = angle, group = replicate, color = recipe)) +
    geom_line(alpha = 0.35) +
    geom_point(alpha = 0.65) +
    facet_wrap(~recipe) +
    labs(
      title = "Profils individuels par replicat",
      x = "Temperature (deg F)",
      y = "Angle de rupture"
    ) +
    theme_minimal()
)

cat("\n2) Commentaire graphique\n")
cat("------------------------\n")
cat(
  "L'angle augmente globalement avec la temperature. ",
  "La recette A semble legerement au-dessus des recettes B et C. ",
  "Les courbes par replicat sont decalees verticalement, ce qui suggere ",
  "un effet aleatoire d'intercept.\n\n",
  sep = ""
)

m_lm <- lm(angle ~ recipe + temp_c, data = cake)
m_ri <- lmer(
  angle ~ recipe + temp_c + (1 | replicate),
  data = cake, REML = TRUE
)
m_ri_ml <- lmer(
  angle ~ recipe + temp_c + (1 | replicate),
  data = cake, REML = FALSE
)
m_rs <- lmer(
  angle ~ recipe + temp_c + (1 + temp_c | replicate),
  data = cake, REML = TRUE
)
m_rs_ml <- lmer(
  angle ~ recipe + temp_c + (1 + temp_c | replicate),
  data = cake, REML = FALSE
)

cat("3) Regression lineaire a effets fixes\n")

print(summary(m_lm))
cat(
  "\nInterpretation : l'ordonnancee a l'origine correspond a la recette A ",
  "a 200 deg F. Les coefficients de B et C sont negatifs par rapport a A ",
  "et la pente de temperature est + (environ 0.158 deg d'angle par deg F). ",
  "Le principal probleme du modele est l'hypothese d'independance : on observe",
  "des mesures repetees sur chaque replicat.\n\n",
  sep = ""
)

cat("4) Modele mixte avec intercept aleatoire\n")

print(summary(m_ri))
cat("\nVariance de l'intercept aleatoire et residuelle\n")
print(VarCorr(m_ri), comp = c("Variance", "Std.Dev."))
cat(
  "\nInterpretation : la variance d'intercept entre replicats est importante, ",
  "donc certains replicats ont un niveau moyen d'angle nettement plus eleve ",
  "ou plus faible que d'autres.\n\n",
  sep = ""
)

cat("5) ranef pour le premier individu\n")

re_1 <- ranef(m_ri)$replicate[1, , drop = FALSE]
print(re_1)
cat(
  "Le premier replicat a un intercept aleatoire predit d'environ ",
  round(re_1[1, 1], 3),
  ".\n\n",
  sep = ""
)

cat("6) Prediction pour recette A et temperature 182.5 deg F\n")

new_obs <- data.frame(
  recipe = factor("A", levels = levels(cake$recipe)),
  temp_c = 182.5 - mean(cake$temp),
  replicate = factor("1", levels = levels(cake$replicate))
)
pred_moyenne <- predict(m_ri, newdata = new_obs, re.form = NA)
pred_rep1 <- predict(m_ri, newdata = new_obs, re.form = NULL)
cat(
  "Prediction populationnelle (sans effet aleatoire) :",
  round(pred_moyenne, 3), "\n"
)
cat(
  "Prediction conditionnelle pour le replicat 1 :",
  round(pred_rep1, 3), "\n\n"
)

cat("7) Intercept aleatoire utile ? Test du rapport de vraisemblance\n")

m_gls <- gls(angle ~ recipe + temp_c, data = cake, method = "ML")
m_lme <- lme(
  angle ~ recipe + temp_c,
  random = ~ 1 | replicate, data = cake, method = "ML"
)
print(anova(m_gls, m_lme))
cat(
  "\nL'ajout d'un intercept aleatoire ameliore tres fortement le modele ",
  "(p-value < 0.0001).\n\n",
  sep = ""
)

cat("8) Modele mixte avec intercept et pente aleatoires\n")

print(summary(m_rs))
cat("\nComparaison RI vs RI+RS\n")
print(anova(m_ri_ml, m_rs_ml))
cat(
  "\nConclusion : la pente aleatoire sur la temperature est quasi nulle et ",
  "n'apporte pas d'amelioration significative.\n\n",
  sep = ""
)

cat("9) Analyse des residus du dernier modele\n")

res_rs <- resid(m_rs)
fit_rs <- fitted(m_rs)
print(shapiro.test(res_rs))
acf_par_rep <- split(
  data.frame(temp = cake$temp, resid = res_rs), cake$replicate
)
lag1 <- sapply(acf_par_rep, function(df) cor(df$resid[-1], df$resid[-nrow(df)]))
cat("\nResume des correlations lag-1 par replicat\n")
print(summary(lag1))

print(
  ggplot(
    data.frame(fitted = fit_rs, resid = res_rs), aes(x = fitted, y = resid)
  ) +
    geom_point(alpha = 0.7) +
    geom_hline(yintercept = 0, linetype = 2) +
    labs(
      title = "Residus vs valeurs ajustees",
      x = "Valeurs ajustees",
      y = "Residus"
    ) +
    theme_minimal()
)

qqnorm(res_rs, main = "QQ-plot des residus")
qqline(res_rs, col = 2)

cat(
  "\nLes residus paraissent acceptables. La normalite n'est pas rejetee ",
  "(Shapiro p > 0.05) et les correlations lag-1 restent modestes.\n\n",
  sep = ""
)


cat("10) IC bootstrap des variances avec bootMer\n")

set.seed(123)
var_fun <- function(fit) {
  c(
    var_intercept = as.data.frame(VarCorr(fit))$vcov[1],
    var_slope = as.data.frame(VarCorr(fit))$vcov[2]
  )
}
boot_var <- bootMer(
  m_rs, var_fun,
  nsim = 200, use.u = FALSE, type = "parametric", seed = 123
)
ci_var <- apply(boot_var$t, 2, quantile, probs = c(0.025, 0.975), na.rm = TRUE)
cat("Estimations ponctuelles\n")
print(boot_var$t0)
cat("\nIC bootstrap 95%\n")
print(ci_var)
cat(
  "\nLa variance d'intercept est clairement non nulle, alors que la ",
  "variance de pente reste proche de zero. L'heterogeneite entre replicats ",
  "porte surtout sur le niveau moyen, pas sur la reponse a la temperature.\n\n",
  sep = ""
)

cat("11) Evolution des predictions selon la temperature et recette\n")

grid_pred <- expand.grid(
  recipe = levels(cake$recipe),
  temp_c = seq(min(cake$temp_c), max(cake$temp_c), length.out = 100)
)
grid_pred$temp <- grid_pred$temp_c + mean(cake$temp)
grid_pred$pred <- predict(m_ri, newdata = grid_pred, re.form = NA)

print(
  ggplot(grid_pred, aes(x = temp, y = pred, color = recipe)) +
    geom_line(linewidth = 1) +
    labs(
      title = "Predictions marginales par recette",
      x = "Temperature (deg F)",
      y = "Angle predit"
    ) +
    theme_minimal()
)

emm_rec <- emmeans(m_ri, pairwise ~ recipe, at = list(temp_c = 0))
print(emm_rec)
cat(
  "\nLes droites sont paralleles.",
  "La recette A presente les predictions les plus elevees en mean, donc elle ",
  "semble la plus resistante a la rupture.\n\n",
  sep = ""
)

cat("12) Modele adequat pour tester l'influence de la recette\n")
cat(
  "Le modele retenu est : angle ~ recipe + temp_c + (1 | replicate). ",
  "Il tient compte de la structure en mesures repetees sans ajouter une pente ",
  "aleatoire inutile. Dans ce modele, les effets de B et C sont negatifs ",
  "par rapport a A. Les contrastes de Tukey indiquent une difference faible entre A et ",
  "B/C, et aucune difference entre B et C.\n",
  sep = ""
)

dev.off()
