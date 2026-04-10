library(mvtnorm)
library(ggplot2)
library(ROCR)

set.seed(42)

# --- Question 2
n <- 1000
Y <- rbinom(n, size = 1, prob = 0.5)

# Question 3
mu0 <- c(0, 0)
sigma0 <- matrix(c(1, 0, 0, 1), nrow = 2)

mu1 <- c(2, 2)
sigma1 <- matrix(c(1, 0.8, 0.8, 1), nrow = 2)

X <- matrix(0, nrow = n, ncol = 2)
X[Y == 0, ] <- rmvnorm(sum(Y == 0), mean = mu0, sigma = sigma0)
X[Y == 1, ] <- rmvnorm(sum(Y == 1), mean = mu1, sigma = sigma1)
colnames(X) <- c("X1", "X2")

# Question 4
data_plot <- data.frame(X, Y = as.factor(Y))
p1 <- ggplot(data_plot, aes(x = X1, y = X2, color = Y)) +
  geom_point(alpha = 0.6) +
  labs(title = "Donnees simulees") +
  theme_minimal()
print(p1)
