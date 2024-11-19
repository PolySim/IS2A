from matplotlib import pyplot as plt
import numpy as np

def f(x):
    return (.5 * x + 3.1) * x - 1.05

def generate_graph_exemple():
    xplot = np.linspace(0, 8, 50)
    yplot = np.array([f(x) for x in xplot])
    plt.plot (xplot, yplot, color='orange', label='graphe de f')
    plt.legend()
    plt.show()

# Question 2
# Question 3

def graph_A():
    A = np.array([-40, 82, -35, -19, 12], dtype=np.float64)
    xplot = np.linspace(-2.5, 2, 50)
    yplot = np.array([np.polynomial.polynomial.polyval(x, A) for x in xplot])
    plt.plot (xplot, yplot, color='orange', label='graphe de f')
    plt.axline((0.1, 0), (-0.1, 0), linewidth=1, color='r')
    plt.legend()
    plt.show()

# Question 4

def eval_naive(x: np.float64, A: np.ndarray[np.float64]):
    amount = 0
    for index, coef in enumerate(A):
        amount += coef * (x ** index)
    return amount

# Question 5

# Elle test si les racines sont correcte

# Question 6

def eval_Horner(x: np.float64, A: np.ndarray[np.float64]):
    if len(A) == 0:
        return 0
    y = A[-1]
    length_A = len(A)
    for i in range(length_A - 2, -1, -1):
        y = y * x + A[i]
    return y

# Il y a n multiplication et n addition


# Question 8

def eval_derivee(x: np.float64, A: np.ndarray[np.float64]):
    if len(A) == 0:
        return 0
    y = A[-1]
    d = 0
    length_A = A[-1]
    for i in range(length_A - 2, -1, -1):
        d += y
        y = y * x + A[i]
    return d

# Question 10

def verif_racine(A: np.ndarray[np.float64], racine: np.ndarray[np.float64]):
    print(np.polynomial.polynomial.polyfromroots(racine), A, sep='\n')
    return np.equal(np.polynomial.polynomial.polyfromroots(racine), A).all()

if __name__ == '__main__':
    # generate_graph_exemple()
    # graph_A()
    print(verif_racine(np.array([-40, 82, -35, -19, 12], dtype=np.float64), np.array([1, -2, 5/4, 4/3], dtype=np.float64)))

    assert verif_racine(np.array([-1, 0, 1], dtype=np.float64), np.array([-1, 1], dtype=np.float64))

    print("All test are passed")