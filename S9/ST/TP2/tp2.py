from enum import Enum

import matplotlib.pyplot as plt
import numpy as np
from statsmodels.tsa.holtwinters import ExponentialSmoothing

ALPHAS = (0.1, 0.3, 0.5, 0.7, 0.9)


class Types(Enum):
    lissage_simple = "lissage_simple"
    lissage_double = "lissage_double"


class Fonction(Enum):
    X1 = "X1"
    X2 = "X2"
    X3 = "X3"


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


epsilon = simulate_normal()
t = np.arange(1, 101)
serie2 = X2(t, epsilon)
serie3 = X3(t, epsilon)
train_test, test_test = epsilon[:70], epsilon[70:]
train_test2, test_test2 = serie2[:70], serie2[70:]
train_test3, test_test3 = serie3[:70], serie3[70:]


def get_dataset(fonction):
    if fonction == Fonction.X1:
        return train_test, test_test
    elif fonction == Fonction.X2:
        return train_test2, test_test2
    elif fonction == Fonction.X3:
        return train_test3, test_test3
    else:
        raise ValueError("Fonction non supportée")


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


def display_results(alpha, previsions, type, fonction):
    plt.plot(np.arange(1, 71), get_dataset(fonction)[0], label="Apprentissage")
    plt.plot(np.arange(71, 101), get_dataset(fonction)[1], label="Test")

    plt.plot(
        np.arange(71, 101),
        previsions,
        color="red",
        linestyle="--",
        label=f"Prévision (α = {alpha})",
    )
    plt.xlabel("Temps")
    plt.ylabel("Valeur")
    plt.title(f"{fonction.name} — Lissage exponentiel {type.name}")
    plt.legend()


def multi_alpha(type, fonction):
    plt.figure(figsize=(12, 9))

    for i, alpha in enumerate(ALPHAS):
        plt.subplot(3, 2, i + 1)
        if type == Types.lissage_simple:
            display_results(
                alpha,
                lissage_simple(get_dataset(fonction)[0], alpha, 30),
                type,
                fonction,
            )
        elif type == Types.lissage_double:
            display_results(
                alpha,
                lissage_double(get_dataset(fonction)[0], alpha, 30),
                type,
                fonction,
            )

    plt.tight_layout()


def calc_erreur(previsions, fonction):
    return np.sum((previsions - get_dataset(fonction)[1]) ** 2)


def display_erreur(type, fonction):
    for alpha in ALPHAS:
        previsions = None
        if type == Types.lissage_simple:
            previsions = lissage_simple(get_dataset(fonction)[0], alpha, 30)
        elif type == Types.lissage_double:
            previsions = lissage_double(get_dataset(fonction)[0], alpha, 30)
        erreur = calc_erreur(previsions, fonction)
        print(f"Erreur pour α = {alpha}: {erreur}")


def lissage_double(X, alpha, horizon):
    modele = ExponentialSmoothing(
        X,
        trend="add",
        seasonal=None,
        initialization_method="estimated",
    )

    resultat = modele.fit(smoothing_level=alpha)

    return resultat.forecast(horizon)


global_data = load_data()

if __name__ == "__main__":
    print()
    # multi_alpha(Types.lissage_simple, Fonction.X1)
    # display_erreur(Types.lissage_simple, Fonction.X1)
    multi_alpha(Types.lissage_double, Fonction.X3)
    display_erreur(Types.lissage_double, Fonction.X3)

    plt.show()
