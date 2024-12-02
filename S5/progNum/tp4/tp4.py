import numpy as np
import matplotlib.pyplot as plt
from scipy.interpolate import CubicSpline
import sys

sys.path.append("/Users/simon/Documents/Developpement/IS2A")

from S5.progNum.tp3.tp3 import display_f
from S5.progNum.tp2.tp2 import eval_Horner


# Question 1

def generate_data() -> tuple[
    np.ndarray[np.float64], np.ndarray[np.float64], np.ndarray[np.float64], np.ndarray[np.float64]]:
    x = np.array([i for i in range(1, 13)], dtype=np.float64)
    y = np.array([8.6, 7, 6.4, 4, 2.8, 1.8, 1.8, 2.3, 3.2, 4.7, 6.2, 7.9], dtype=np.float64)
    x_without3 = np.concatenate((x[:2], x[3:]))
    y_without3 = np.concatenate((y[:2], y[3:]))
    x_with13 = np.concatenate((x, [13]))
    y_with13 = np.concatenate((y, [y[0]]))
    return x, y, x_without3, y_without3, x_with13, y_with13


def display_interpollation() -> None:
    x, y, x_without3, y_without3 = generate_data()
    display_f(x=x, y=y, x2=x_without3, y2=y_without3, start=1, end=12, nb_point=2000)


# Question 2

# ai = y[i]

# Question 3

def h(i: int, x=generate_data()[0]) -> np.float64:
    return x[i + 1] - x[i]


# Question 4

def generate_A() -> np.ndarray[np.float64]:
    x = generate_data()[0]
    x_length = len(x)
    A = np.zeros((x_length - 2, x_length - 2), dtype=np.float64)
    for i in range(x_length - 2):
        A[i][i] = 2 * ((h(i) / 3) + (h(i + 1) / 3))
        if i < x_length - 3:
            A[i][i + 1] = h(i + 1) / 3
            A[i + 1][i] = h(i + 1) / 3
    return A


def generate_v() -> np.ndarray[np.float64]:
    x, y = generate_data()[:2]
    x_length = len(x)
    v = np.zeros(x_length - 2, dtype=np.float64)
    for i in range(x_length - 2):
        v[i] = (y[i] / h(i)) \
               - ((1 / h(i)) + (1 / h(i + 1))) * y[i + 1] \
               + (y[i + 2] / h(i + 1))
    return v


def generate_c() -> np.ndarray[np.float64]:
    return np.linalg.solve(generate_A(), generate_v())


# Question 5

def generate_c_with_0() -> np.ndarray[np.float64]:
    return np.concatenate(([0], generate_c(), [0]))


# Question 6

def generate_vect_d(x=generate_data()[0], c=generate_c_with_0()) -> np.ndarray[np.float64]:
    x_length = len(x)
    d = np.zeros(x_length - 1, dtype=np.float64)
    for i in range(x_length - 1):
        d[i] = (c[i + 1] - c[i]) / (3 * h(i))
    return d


def generate_vect_b(x=generate_data()[0], y=generate_data()[1], c=generate_c_with_0(), d=generate_vect_d()) -> \
        np.ndarray[np.float64]:
    x_length = len(x)
    b = np.zeros(x_length - 1, dtype=np.float64)
    for i in range(x_length - 1):
        b[i] = ((y[i + 1] - y[i]) / h(i)) \
               - c[i] * h(i) \
               - d[i] * (h(i) ** 2)
    return b


# Question 7

def find_index(z: np.float64, x=generate_data()[0]) -> int:
    for i in range(len(x) - 1):
        if i + 1 == len(x):
            return i
        if x[i] <= z <= x[i + 1]:
            return i
    return 0


def eval_spline_naturelle(z: np.float64) -> np.float64:
    x, y = generate_data()[:2]
    b = generate_vect_b()
    c = generate_c_with_0()
    d = generate_vect_d()
    i = find_index(z)
    return eval_Horner(z - x[i], np.array([y[i], b[i], c[i], d[i]], dtype=np.float64))


def display_spline_naturelle() -> None:
    x, y = generate_data()[:2]
    xplot = np.linspace(1, 12, 1000)
    yplot = np.array([eval_spline_naturelle(z) for z in xplot])

    spline = CubicSpline(x, y, bc_type='natural')
    y_spline = spline(xplot)

    # Tracé des deux interpolations
    plt.figure(figsize=(10, 6))
    plt.plot(xplot, yplot, label="Spline Naturelle (perso)", color="orange")
    plt.plot(xplot, y_spline, label="Spline Naturelle (CubicSpline)", color="blue", linestyle="--")
    plt.scatter(x, y, color="red", label="Points de données", zorder=5)  # Marquer les points d'origine
    plt.xlabel("x")
    plt.ylabel("f(x)")
    plt.title("Comparaison : Perso vs Spline Naturelle")
    plt.legend()
    plt.grid(True)
    plt.show()


# Question 9

def display_spline_periodic() -> None:
    x, y = generate_data()[4:6]
    xplot = np.linspace(1, 20, 1000)
    yplot = np.array([eval_spline_periodic(z) for z in xplot])

    spline = CubicSpline(x, y, bc_type='periodic')
    y_spline = spline(xplot)
    plt.plot(xplot, yplot, label="Spline Periodic (perso)", color="orange")
    plt.plot(xplot, y_spline, label="Spline Periodic (CubicSpline)", color="blue", linestyle="--")
    plt.scatter(x, y, color="red", label="Points de données", zorder=5)
    plt.xlabel("x")
    plt.ylabel("f(x)")
    plt.legend()
    plt.grid(True)
    plt.show()


# Question 10

def generate_A_periodic() -> np.ndarray[np.float64]:
    x = generate_data()[4]
    x_length = len(x)
    A = np.zeros((x_length - 2, x_length - 2), dtype=np.float64)
    for i in range(x_length - 2):
        A[i][i] = h(i, x) / 3
        if (i + 1) < x_length - 2:
            A[i][i + 1] = 2 * ((h(i, x) / 3) + (h(i + 1, x) / 3))
        if (i + 2) < x_length - 2:
            A[i][i + 2] = h(i + 1, x) / 3
    A[-2][0] = h(x_length - 3, x) / 3
    A[-1][0] = 2 * ((h(x_length - 3, x) / 3) + (h(0, x) / 3))
    A[-1][1] = h(0, x) / 3
    return A


def generate_v_periodic() -> np.ndarray[np.float64]:
    x, y = generate_data()[4:6]
    x_length = len(x)
    v = np.zeros(x_length - 2, dtype=np.float64)
    for i in range(x_length - 2):
        v[i] = (y[i] / h(i, x)) \
               - ((1 / h(i, x)) + (1 / h(i + 1, x))) * y[i + 1] \
               + (y[i + 2] / h(i + 1, x))
    v[-1] = (y[x_length - 2] / h(x_length - 2, x)) \
            - ((1 / h(x_length - 2, x)) + (1 / h(0, x))) * y[0] \
            + (y[1] / h(0, x))
    return v


def generate_c_periodic() -> np.ndarray[np.float64]:
    c_partial = np.linalg.solve(generate_A_periodic(), generate_v_periodic())
    return np.concatenate((c_partial, [c_partial[0]]))


# Question 11

def generate_d_periodic() -> np.ndarray[np.float64]:
    x = generate_data()[4]
    c = generate_c_periodic()
    x_length = len(x)
    d = np.zeros(x_length - 2, dtype=np.float64)
    for i in range(x_length - 3):
        d[i] = (c[i + 1] - c[i]) / (3 * h(i))
    d[-1] = (c[0] - c[i]) / (3 * h(i))
    return d


def generate_b_periodic() -> np.ndarray[np.float64]:
    x, y = generate_data()[4:6]
    c = generate_c_periodic()
    d = generate_d_periodic()
    x_length = len(x)
    b = np.zeros(x_length - 2, dtype=np.float64)
    for i in range(x_length - 2):
        b[i] = ((y[i + 1] - y[i]) / h(i)) \
               - c[i] * h(i) \
               - d[i] * (h(i) ** 2)
    return b


# Question 12

def eval_spline_periodic(z: np.float64) -> np.float64:
    x, y = generate_data()[4:6]
    b = generate_b_periodic()
    c = generate_c_periodic()
    d = generate_d_periodic()
    while z > x[-2]:
        z -= x[-1] - x[0]
    i = find_index(z)
    return eval_Horner(z - x[i], np.array([y[i], b[i], c[i], d[i]], dtype=np.float64))


if __name__ == "__main__":
    # display_interpollation()
    assert h(0) == 1


    def generate_eval_spline() -> np.ndarray[np.float64]:
        x = generate_data()[0]
        return np.array([eval_spline_naturelle(z) for z in x], dtype=np.float64)


    assert np.allclose(generate_eval_spline(), generate_data()[1])

    # display_spline_naturelle()
    display_spline_periodic()


    def generate_eval_spline_periodique() -> np.ndarray[np.float64]:
        x = generate_data()[4]
        return np.array([eval_spline_periodic(z) for z in x], dtype=np.float64)


    print(eval_spline_periodic(12.5))
    # print(generate_eval_spline_periodique(), generate_data()[5], eval_spline_periodic(12.5))

    print("All tests passed")
