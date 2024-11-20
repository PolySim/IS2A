import numpy as np

# Question 1

# Dans Array object -> scalar
def question1():
    print("32 ---  min : ", np.finfo(np.float32).min, " max : ", np.finfo(np.float32).max)
    print("64 ---  min : ", np.finfo(np.float64).min, " max : ", np.finfo(np.float64).max)
    a = np.float32(3)
    print("type de a : ", np.dtype(a))
    print("Generation de inf par calcul : np.float32(1)/np.float32(0) = ", np.float32(1)/np.float32(0))
    print("Generation de nan par calcul : np.float32(0)/np.float32(0) = ", np.float32(0)/np.float32(0))

#32 ---  min :  -3.4028235e+38  max :  3.4028235e+38
#64 ---  min :  -1.7976931348623157e+308  max :  1.7976931348623157e+308
#type de a :  float32
#Generation de inf par calcul : np.float32(1)/np.float32(0) =  inf
#Generation de nan par calcul : np.float32(0)/np.float32(0) =  nan



# Question 2
def question2():
    a = np.float32(1e7)
    b = np.float32(25.1234)
    c = np.float32(10000025)
    print(a + b - c)
    print(a - c + b)

# Le deuxième calcul donne un résultat plus précis que le premier.
# Après a + b il fait un arrondi à l'unité car il n'a pas assez de place pour stocker les décimales


# Question 3
def question3():
    x1 = np.float64(1.23456e8)
    x2 = np.float64(1.3579177e-10)
    a = np.float64(1)
    b = - x1 - x2
    c = x1 * x2
    delta = b**2 - 4*a*c
    x1bis = (-b + np.sqrt(delta)) / (2*a)
    x2bis = (-b - np.sqrt(delta)) / (2*a)
    print("x1 = ", x1, " x1bis = ", x1bis)
    print("x2 = ", x2, " x2bis = ", x2bis)
    print("x2 par c / x1", c/x1bis)

# x1 =  123456000.0  x1bis =  123456000.0
# x2 =  13579177.0  x2bis =  13579177.0
# On remplace l'exposant 7 de x2 par - 10
# x1 =  123456000.0  x1bis =  123456000.0
# x2 =  1.3579177e-10  x2bis =  0.0
# On perd de la précision sur x2bis, elle a été arrondie à 0.0
# x2 par c / x1bis 1.3579177e-10


# Question 4
def question4(nb_tour = 80):
    a, b = np.float64(0), np.float64(1)
    x = np.empty (nb_tour + 1, dtype=np.float64)
    x[0] = a + b
    x[1] = 2 * a + b / 2
    for i in range(2, nb_tour + 1):
        x[i] = (np.float64(5) / np.float64(2)) * x[i - 1] - x[i - 2]
    return x

# a2^n est égal à 0 la limite est donc b/2n, cette fonction tend vers 0 en +infini

# Question 5
# Avec b = 1/3, la suite tend vers -infini et non 0
# A un moment donné la suite a été arrondi à 0, ce qui a fait que la suite a divergé

# Question 6
def question6(nb_tour = 80):
    a, b = np.float64(0), np.float64(1)
    x0 = a + b
    x1 = 2 * a + b / 2
    for i in range(2, nb_tour + 1):
        x0, x1 = x1, (np.float64(5) / np.float64(2)) * x1 - x0
    return x1

if __name__ == '__main__':
    # question1()
    # question2()
    # question3()
    # print(question4())
    # print(question6())

    assert question6() == question4()[-1]

    print("All tests passed")
















