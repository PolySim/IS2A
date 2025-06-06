# Question 1

# Lecture des données
data <- read.csv("/Users/simondesdevises/Documents/IS2A/S6/StatInf/TP1/fiabilites.csv")
X <- data[[1]]

# Fonction pour tracer les histogrammes
trace_histos <- function(X, tailles) {
  par(mfrow=c(1,length(tailles))) # Affiche plusieurs histogrammes côte à côte (dans notre cas 3)
  for (n in tailles) {
    echantillon <- sample(X, n) # Tire un échantillon aléatoire de taille n dans la liste X
    hist(echantillon, breaks = 10, main = paste("Histogramme n =", n), xlab = "Temps")
  }
}

# Appel avec tailles 30, 50, 80
trace_histos(X, c(30, 50, 80))


# Question 2

log_vraisemblance <- function(params, data) {
  mu <- params[1]
  sigma <- params[2]
  
  if (sigma <= 0) return(-Inf)  # σ doit être strictement positif
  
  # Calcul de la somme des log-densités de la loi log-normale pour les données
  # dlnorm fonction de densité de la loi normale
  # log = TRUE car on veut le logarithme de la densité
  sum(dlnorm(data, meanlog = mu, sdlog = sigma, log = TRUE))
}


# Question 3

# Cette fonction estime les paramètres en maximisant la log-vraisemblance avec optim
estimation_MV <- function(echantillon) {
  # calcule la moyenne des logarithmes des données
  moyenne = mean(log(echantillon))
  # calcule l'écart-type des logarithmes des données
  ecart_type = sd(log(echantillon))
  # Valeurs initiales pour l’optimisation
  init <- c(moyenne, sd(log(echantillon)))
  # Optimise -log car on veut le maximum de la log-vraisemblance
  opt <- optim(par = init, fn = function(p) -log_vraisemblance(p, echantillon))
  return(opt$par)
}

# Estimations pour les 3 tailles
set.seed(42)  # Fixe la graine aléatoire pour reproductibilité
ech30 <- sample(X, 30)
ech50 <- sample(X, 50)
ech80 <- sample(X, 80)

params30 <- estimation_MV(ech30)
params50 <- estimation_MV(ech50)
params80 <- estimation_MV(ech80)

params30; params50; params80


# Question 4

# Cette fonction applique les formules théoriques :
# mu = moyenne des log(Xi), sigma = écart-type des log(Xi)
estimateurs_theoriques <- function(echantillon) {
  logs <- log(echantillon)
  mu_hat <- mean(logs)
  sigma_hat <- sqrt(mean((logs - mu_hat)^2))
  return(c(mu_hat, sigma_hat))
}

estimateurs_theoriques(ech30)
estimateurs_theoriques(ech50)
estimateurs_theoriques(ech80)


# Question 5

# Pour une loi log-normale, on peut inverser la moyenne/variance pour retrouver mu et sigma
# Cette fonction les calcule à partir des moments empiriques
estimateurs_moments <- function(echantillon) {
  m <- mean(echantillon) # Moyenne empirique
  v <- var(echantillon) # Variance empirique
  sigma2_hat <- log(1 + v / m^2) # Inversion de la formule de la variance
  mu_hat <- log(m) - sigma2_hat / 2 # Inversion de la formule de l'espérance
  return(c(mu_hat, sqrt(sigma2_hat)))
}

estimateurs_moments(ech30)
estimateurs_moments(ech50)
estimateurs_moments(ech80)