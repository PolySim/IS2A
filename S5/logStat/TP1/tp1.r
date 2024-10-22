# Exercice 1
# Variable numérique
# a
(var_pi <- pi)

# b
(var_pi <- round(var_pi, 2))


# Variable caractère
# a
(mot1 <- "Aiguille")

# b
(mot2 <- "de")

# c
(mot3 <- "BUFFON")

# e
library(stringi)
library(stringr)
(experience <- paste(str_to_upper(mot1), str_to_title(mot2), str_to_lower(mot3)))


# Fonction

Buffon <- function(n, a, x, L){
  return((2 * n * a) / (x * L))
}

Buffon(25, 0.19, 6, 0.5)
Buffon(1000, 0.19, 241, 0.5)


# Vecteur

A <- c(7, 26, 7, 18, 22, 18, 7)
B <- c(8, 4, 8, 2, 4, 5, 13)
C <- c(10, 20)
D <- c(rep(7, 50))
E <- c(seq(0, 50, 2))

i <- which(A>=25)
A[i] <- 0

2*A + 4
sum(A)
prod(A)
range(A) #minMax
length(A)
summary(A)

A + B # Seulement si meme taille 

cbind(A, B)
rbind(A, B)
c(A, B) # Concact
table(A)
sort(A)


# Fonction 2

Quadratique <- function(x, w){
  return(sum(x * w) / sum(w))
}

x1 <- c(10, 20, 10)
w1 <- c(1, 2, 1)
Quadratique(x1, w1)


# Matrices

M <- matrix(c(1,2,3,4), nrow = 2, ncol = 2)
M
M[1,2]
M[,2]

N <- matrix(c(10,20,30,40), nrow = 2, ncol = 2)
N

V <- c(1, 0, 3, 4, 5, 5, 0, 4, 5, 6, 3, 4, 0, 1, 3, 2)
V
P <- matrix(V, nrow=4, ncol=4)

M*N
M%*%N # Produit matricielle


C <- diag(c(12, 45, 17, 6))
C

P1 <-  P[, apply(P, 2, function(col) all(col < 3.5))]
P
P1





