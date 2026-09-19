from enum import Enum

import matplotlib.pyplot as plt
import numpy as np
from statsmodels.tsa.holtwinters import ExponentialSmoothing

ALPHAS = (0.1, 0.3, 0.5, 0.7, 0.9)


class Types(Enum):
    lissage_simple = "lissage_simple"
    lissage_double = "lissage_double"
    hw_additif = "hw_additif"
    hw_multiplicatif = "hw_multiplicatif"


class Fonction(Enum):
    X1 = "X1"
    X2 = "X2"
    X3 = "X3"


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
    train, test = get_dataset(fonction)
    plt.figure(figsize=(12, 9))

    for i, alpha in enumerate(ALPHAS):
        plt.subplot(3, 2, i + 1)
        previsions = calculer_previsions(train, alpha, len(test), type)
        display_results(alpha, previsions, type, fonction)

    plt.tight_layout()


def calc_erreur(previsions, fonction):
    return np.sum((previsions - get_dataset(fonction)[1]) ** 2)


def display_erreur(type, fonction):
    train, test = get_dataset(fonction)
    erreurs = []

    for alpha in ALPHAS:
        previsions = calculer_previsions(train, alpha, len(test), type)
        erreur = calc_erreur(previsions, fonction)
        erreurs.append(erreur)
        print(f"Erreur pour α = {alpha}: {erreur}")

    meilleur = np.argmin(erreurs)
    print(f"Meilleur α : {ALPHAS[meilleur]}")


def lissage_double(X, alpha, horizon):
    modele = ExponentialSmoothing(
        X,
        trend="add",
        seasonal=None,
        initialization_method="estimated",
    )

    resultat = modele.fit(smoothing_level=alpha)

    return resultat.forecast(horizon)


def holt_winters(X, alpha, horizon, seasonal):
    decalage = 0

    # Le multiplicatif nécessite des observations positives.
    if seasonal == "mul" and np.min(X) <= 0:
        decalage = 1 - np.min(X)

    modele = ExponentialSmoothing(
        X + decalage,
        trend="add",
        seasonal=seasonal,
        seasonal_periods=12,
        initialization_method="estimated",
    )

    resultat = modele.fit(smoothing_level=alpha)

    return resultat.forecast(horizon) - decalage


def calculer_previsions(train, alpha, horizon, type):
    if type == Types.lissage_simple:
        return lissage_simple(train, alpha, horizon)
    elif type == Types.lissage_double:
        return lissage_double(train, alpha, horizon)
    elif type == Types.hw_additif:
        return holt_winters(train, alpha, horizon, "add")
    elif type == Types.hw_multiplicatif:
        return holt_winters(train, alpha, horizon, "mul")

    raise ValueError("Type de lissage non supporté")


if __name__ == "__main__":
    print()
    # multi_alpha(Types.lissage_simple, Fonction.X1)
    # display_erreur(Types.lissage_simple, Fonction.X1)
    # multi_alpha(Types.lissage_double, Fonction.X3)
    # display_erreur(Types.lissage_double, Fonction.X3)
    # multi_alpha(Types.hw_additif, Fonction.X3)
    # display_erreur(Types.hw_additif, Fonction.X3)
    # multi_alpha(Types.hw_multiplicatif, Fonction.X3)
    # display_erreur(Types.hw_multiplicatif, Fonction.X3)

    plt.show()
