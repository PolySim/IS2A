import numpy as np
import matplotlib.pyplot as plt

# Question 1

def generate_data():
    x = np.array([1, 2, 3, 5], dtype=np.float64)
    y = np.array([1, 4, 2, 5], dtype=np.float64)
    A = np.vander(x, increasing=True)
    return x, y , A

def solve_system():
    x, y, A = generate_data()
    return np.linalg.solve(A, y)

#  Question 2

def vandermonde():
    x = generate_data()[0]
    x_length = len(x)
    return np.array([[x[i] ** j for j in range(x_length)] for i in range(x_length)], dtype=np.float64)

#  Question 3

#  En bas à droit

# Question 4

# Ce sont des réponses valable pour certains points

# Question 5

def eval_neville(x: np.ndarray[np.float64], y: np.ndarray[np.float64], z: np.float64):
    P = np.zeros((len(x), len(x)), dtype=np.float64)
    for i in range(len(x)):
        for j in range(i + 1):
            if j == 0:
                P[i][j] = y[i]
            else:
                P[i][j] = ((x[i] - z) * P[i - 1][j - 1] + (z - x[i - j]) * P[i][j - 1]) / (x[i] - x[i - j])
    return P[-1][-1]

# Question 7

def generate_divise(x: np.ndarray[np.float64], y: np.ndarray[np.float64]):
    C = np.zeros((len(x), len(x)), dtype=np.float64)
    for i in range(len(x)):
        for j in range(i + 1):
            if j == 0:
                C[i][j] = y[i]
            else:
                C[i][j] = (C[i][j - 1] - C[i - 1][j - 1]) / (x[i] - x[i - j])
    return C

def eval_newton_inter(x: np.ndarray[np.float64], c: np.ndarray[np.ndarray[np.float64]], z: np.float64):
    x_length = len(x)
    y = c[-1][-1]
    for i in range(x_length - 2, -1, -1):
        y = y * (z - x[i]) + c[i][i]
    return y

def display_f(x: np.ndarray[np.float64], y: np.ndarray[np.float64], start = 0, end = 8, x2: np.ndarray = None,
    y2: np.ndarray = None, nb_point=50):
    c = generate_divise(x, y)
    xplot = np.linspace(start, end, nb_point)
    yplot = np.array([eval_newton_inter(x, c, x_bis) for x_bis in xplot])
    plt.plot(xplot, yplot, color='orange', label='graphe de f')

    if x2 is not None and y2 is not None:
        c = generate_divise(x2, y2)
        yplot = np.array([eval_newton_inter(x2, c, x_bis) for x_bis in xplot])
        plt.plot(xplot, yplot, color='blue', label='graphe de g')
    plt.legend()
    plt.show()

if __name__ == "__main__":
    x, y , A = generate_data()
    assert np.equal(vandermonde(), A).all()

    def generate_eval_neville():
        return np.array([eval_neville(x, y, x[i]) for i in range(len(x))], dtype=np.float64)

    assert np.equal(generate_eval_neville(), y).all()

    def generate_eval_newton_inter():
        c = generate_divise(x, y)
        return np.array([eval_newton_inter(x, c, x[i]) for i in range(len(x))], dtype=np.float64)

    assert np.allclose(generate_eval_newton_inter(), y)

    display_f(x, y)

    print("All test are passed")