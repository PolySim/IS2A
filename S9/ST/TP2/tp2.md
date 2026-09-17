# TP 2

## PT 1

### Q1

```python
import matplotlib.pyplot as plt
import numpy as np
from statsmodels.tsa.holtwinters import ExponentialSmoothing

def simulate_normal(size=100):
    return np.random.normal(0, 1, size)

data_norm = simulate_normal()
train_test, test_test = data_norm[:70], data_norm[70:]

def lissage_simple(X, alpha, horizon):
    sum = 0
    for j in range(len(X)):
        sum += (1 - alpha) ** j * X[len(X) - j - 1]
    return np.full(horizon, alpha * sum)


def lissage_simple_with_lib(X, alpha, horizon):
    modele = ExponentialSmoothing(
        train_test,
        trend=None,
        seasonal=None,
        initialization_method="known",
        initial_level=train_test[0],
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
        label="Prévision (α = 0.5)",
    )
    plt.xlabel("Temps")
    plt.ylabel("Valeur")
    plt.title("X1 — Lissage exponentiel simple")
    plt.legend()
```

### Q2

```python
ALPHAS = (0.1, 0.3, 0.5, 0.7, 0.9)

def multi_alpha():
    plt.figure(figsize=(12, 9))

    for i, alpha in enumerate(ALPHAS):
        plt.subplot(3, 2, i + 1)
        display_results(alpha)

    plt.tight_layout()
```

α Interprétation
━━━━━ ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
0,1 Lissage fort : les observations anciennes gardent du
poids.
───── ─────────────────────────────────────────────────────────
0,3 Davantage de poids aux observations récentes.
───── ─────────────────────────────────────────────────────────
0,5 Poids de 50 % à la dernière observation.
───── ─────────────────────────────────────────────────────────
0,7 Prévision fortement influencée par les dernières
observations.
───── ─────────────────────────────────────────────────────────
0,9 Prévision très proche de la dernière observation
d’apprentissage.

Pour X1, qui est un bruit blanc centré en zéro, on attend une
prévision proche de zéro. Graphiquement, cherche la droite
autour de laquelle les observations du test semblent le mieux
réparties, avec des écarts globalement faibles.

### Q3

```python
def calc_erreur(previsions):
    return np.sum((previsions - test_test) ** 2)


def display_erreur():
    for alpha in ALPHAS:
        previsions = lissage_simple(train_test, alpha, 30)
        erreur = calc_erreur(previsions)
        print(f"Erreur pour α = {alpha}: {erreur}")

# Erreur pour α = 0.1: 32.83244711845312
# Erreur pour α = 0.3: 37.35158484171907
# Erreur pour α = 0.5: 46.64839739433848
# Erreur pour α = 0.7: 63.0977654920502
# Erreur pour α = 0.9: 86.62582046649213
```

On constate que l'erreur est minimisée pour α = 0.1.

### Q4

```python
def lissage_double(X, alpha, horizon):
    modele = ExponentialSmoothing(
        X,
        trend="add",
        seasonal=None,
        initialization_method="estimated",
    )

    resultat = modele.fit(smoothing_level=alpha)

    return resultat.forecast(horizon)
```
