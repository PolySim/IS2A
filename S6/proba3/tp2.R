N = 10000
i = 0

x_coor = c()
y_coor = c()

while (i < N) {
  U = runif(1)
  V = runif(1)

  X = sqrt(-2 * log(U)) * cos(2 * pi * V)
  Y = sqrt(-2 * log(U)) * sin(2 * pi * V)
  
  x_coor = c(X, x_coor)
  y_coor = c(Y, y_coor)
  
  i = i + 1
}

plot(x_coor, y_coor, asp=1, pch=16)