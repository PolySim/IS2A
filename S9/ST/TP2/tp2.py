from sre_compile import dis

import matplotlib.pyplot as plt
import numpy as np
from statsmodels.tsa.holtwinters import ExponentialSmoothing

ALPHAS = (0.1, 0.3, 0.5, 0.7, 0.9)


def load_data(filename="co2.txt"):
    with open(filename, "r") as f:
        data = f.readlines()
        res = np.array([float(line.strip()) for line in data])
        f.close()
        return res


def simulate_normal(size=100):
    return np.random.normal(0, 1, size)


def X1(epsillon):
    return epsillon


def X2(t, epsillon):
    return 0.5 * t + 2 * epsillon


def X3(t, epsillon):
    return 0.5 * t + 3 * np.cos(t * np.pi / 6) + epsillon


data_norm = simulate_normal()
train_test, test_test = data_norm[:70], data_norm[70:]


def lissage_simple(X, alpha, horizon):
    sum = 0
    for j in range(len(X)):
        sum += (1 - alpha) ** j * X[len(X) - j - 1]
    return np.full(horizon, alpha * sum)


def lissage_simple_with_lib(X, alpha, horizon):
    modele = ExponentialSmoothing(
        X,
        trend=None,
        seasonal=None,
        initialization_method="known",
        initial_level=X[0],
    )

    resultat = modele.fit(smoothing_level=alpha, optimized=False)
    return resultat.forecast(horizon)


def display_results(alpha):
    previsions = lissage_simple(train_test, alpha, 30)

    plt.plot(np.arange(1, 71), train_test, label="Apprentissage")
    plt.plot(np.arange(71, 101), test_test, label="Test")

    plt.plot(
        np.arange(71, 101),
        previsions,
        color="red",
        linestyle="--",
        label=f"Prévision (α = {alpha})",
    )
    plt.xlabel("Temps")
    plt.ylabel("Valeur")
    plt.title("X1 — Lissage exponentiel simple")
    plt.legend()


def multi_alpha():
    plt.figure(figsize=(12, 9))

    for i, alpha in enumerate(ALPHAS):
        plt.subplot(3, 2, i + 1)
        display_results(alpha)

    plt.tight_layout()


def calc_erreur(previsions):
    return np.sum((previsions - test_test) ** 2)


def display_erreur():
    for alpha in ALPHAS:
        previsions = lissage_simple(train_test, alpha, 30)
        erreur = calc_erreur(previsions)
        print(f"Erreur pour α = {alpha}: {erreur}")


global_data = load_data()

if __name__ == "__main__":
    print()
    # multi_alpha()
    display_erreur()

    # plt.show()
