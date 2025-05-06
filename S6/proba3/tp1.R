# Exercice 1 

c = 3 / 2 

f <- function(x) {
  c/((1 + x) ^ 2)
}

F <- function(a) {
  if (a < 0) { return (0) }
  if (a > 2) { return (1) }
  return ((3*a) / (2 + 2*a))
}

F(2)
F(-1)
F(3)

F_1 <- function(y) {
  if (y > 1) return (2)
  if (y < 0) return (0)
  return ( ((2/3) * y) /  (1 - (2/3) * y) )
}

F_1(3)
F_1(1)
F_1(0.75)

y_F_1 = c()
x_unif = c()
for (x in 1:10000) {
  x = runif(1)
  x_unif = c(x, x_unif)
  y_F_1 = c(F_1(x), y_F_1)
}


hist(y_F_1, breaks = 50, freq = FALSE)
curve(f, 0, 2, add = TRUE)

# Exercice 2

f_cauchy <- function(x) {
 return (1 / (pi * (1 + x^2))) 
}

F_cauchy <- function(a) {
  return (tan(pi * (a - 0.5)))
}

U = runif(1000)
Y = tan(pi * (U - 0.5))
Z = cumsum(Y) / seq(1, 1000, 1)
plot(seq(1, 1000, 1), Z, type='l')

# Exercices : 3

c2 = exp(1) / (exp(1) - 1)

g <- function (x) {
  if (x < 0 || x > 1) { return (0) }
  return (x)
}

f <- function (x) {
  return (exp(x) / (exp(1) - 1))
}

h <- function (x) {
  if (x < 0 || x > 1) { return (0) }
  return (exp(x) / exp(1))
}

x_unif = c()
h_x = c()
count = 0
while (count < 20000) {
  x = runif(1)
  y = runif(1)
  if (x <= h(y)) {
    count = count + 1
    x_unif = c(x, x_unif)
    h_x = c(y, h_x)
  }
}

hist(h_x, breaks = 50, freq = FALSE)
curve(f, 0, 1, add = TRUE)

# Exercice 4

x_coor = c()
y_coor = c()

for (i in 1:1000) {
  x = runif(1, -1, 1)
  y = runif(1, -1, 1)
  x_coor = c(x, x_coor)
  y_coor = c(y, y_coor)
}

plot(x_coor, y_coor, pch=16, col="#A23456")



x_coor = c()
y_coor = c()
moyenne = 0

d <- function (x1, x2) {
  return (sqrt(x1^2 + x2^2))
}

n = 1000
i = 0
count = 0

while (i< n) {
  x = runif(1, -1, 1)
  y = runif(1, -1, 1)
  count = count + 1
  if (d(x, y) <= 1) {
    x_coor = c(x, x_coor)
    y_coor = c(y, y_coor)
    i = i + 1
    moyenne = moyenne + d(x, y)
  }
}

#for (i in 1:n) {
#  x = runif(1, -1, 1)
#  y = runif(1, -sqrt((1 - x^2)), sqrt((1 - x^2)))
#  moyenne = moyenne + sqrt(x^2 + y^2)
#  x_coor = c(x, x_coor)
#  y_coor = c(y, y_coor)
#}

moyenne = moyenne / n

plot(x_coor, y_coor, pch=16, col="#A23456", asp=1)








