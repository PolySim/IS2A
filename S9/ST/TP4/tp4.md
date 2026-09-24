# TP 4

## Partie 1

### Question 1

```py
import matplotlib.pyplot as plt
import numpy as np
from statsmodels.graphics.tsaplots import plot_acf, plot_pacf


def load_data(filename="data/AR.txt"):
    with open(filename, "r") as f:
        data = f.readlines()
        res = np.array([float(line.strip()) for line in data])
        f.close()
        return res


def auto_correlation(data):
    fig, axes = plt.subplots(1, 2, figsize=(12, 4))
    plot_acf(data, lags=40, ax=axes[0], alpha=0.05, zero=False)
    plot_pacf(data, lags=40, ax=axes[1], alpha=0.05, zero=False, method="ywm")
    axes[0].set_title("Autocorrélation (ACF)")
    axes[1].set_title("Autocorrélation partielle (PACF)")
    for ax in axes:
        ax.set_xlabel("Retard")
    fig.tight_layout()
```

En regardant l'autocorrélation on observe qu'elle diminue vers `0` ce qui fait bien penser à
un processus `AR`.
En regardant l'autocorrélation partielle on observe qu'elle à 2 pics ce qui fait bien penser à
un processus `AR(2)`.

### Question 2

```py
from statsmodels.tsa.arima.model import ARIMA

def estime_params_AR(data, order):
    model = ARIMA(data, order=order)
    results = model.fit()
    return results

results = estime_params_AR(data, order=(2, 0, 0))
print(results.summary())
```

```
SARIMAX Results
==============================================================================
Dep. Variable:                      y   No. Observations:                  500
Model:                 ARIMA(2, 0, 0)   Log Likelihood                -718.125
Date:                Thu, 24 Sep 2026   AIC                           1444.251
Time:                        14:16:25   BIC                           1461.109
Sample:                             0   HQIC                          1450.866
 - 500
Covariance Type:                  opg
==============================================================================
coef    std err          z      P>|z|      [0.025      0.975]
------------------------------------------------------------------------------
const          0.0427      0.179      0.239      0.811      -0.308       0.393
ar.L1          1.0014      0.041     24.601      0.000       0.922       1.081
ar.L2         -0.2555      0.043     -5.900      0.000      -0.340      -0.171
sigma2         1.0328      0.072     14.255      0.000       0.891       1.175
===================================================================================
Ljung-Box (L1) (Q):                   0.04   Jarque-Bera (JB):                 2.92
Prob(Q):                              0.84   Prob(JB):                         0.23
Heteroskedasticity (H):               0.83   Skew:                            -0.03
Prob(H) (two-sided):                  0.23   Kurtosis:                         2.63
===================================================================================
```

On a mis `order=(2, 0, 0)` car `p=2` &rarr; Nombre de retards de la série dans la partie `AR`.
`d=0` &rarr; Pas de différentiation appliquée à la série.
`q=0` &rarr; Nombre de retards du bruit dans la partie `MA`.

**Explication des résultats**

`const` &rarr; l'espérance de la série. \
`sigma2` &rarr; variance de la série. \
`ar.L1` &rarr; ϕ₁, coefficient du retard 1 \
`ar.L2` &rarr; ϕ₂, coefficient du retard 2

### Question 3

```python
def calcule_racine(phi1, phi2):
    return np.roots([-phi2, -phi1, 1])


def is_stationary():
    return np.all(np.abs(calcule_racine(1, -1 / 4)) > 1)
```

La fonction `is_stationary()` renvoie `True` si la série est stationnaire.

### Question 4

```python
def display_residus(residus):
    fig, axes = plt.subplots(1, 2, figsize=(12, 4))

    axes[0].plot(residus)
    axes[0].axhline(0, color="red", linestyle="--")
    axes[0].set_title("Résidus")

    plot_acf(residus, lags=40, ax=axes[1], zero=False)
    axes[1].set_title("ACF des résidus")

    plt.tight_layout()
```

- Les résidus fluctuent autour de zéro, sans tendance ni périodicité visible.
- Leur dispersion semble relativement constante au cours du temps.
- L’ACF est proche de zéro à presque tous les retards, sans structure
  persistante.
- Un pic dépasse légèrement la bande de confiance vers le retard 8. Un
  dépassement isolé parmi 40 retards n’est pas inhabituel avec des bandes à 95
  % et ne suffit pas à remettre en cause le modèle.

Les résidus sont visuellement centrés autour de zéro, de variance approximativement constante et ne présentent pas d’autocorrélation notable persistante. Ils sont donc compatibles avec un bruit blanc. La modélisation AR(2) semble ainsi bien rendre compte de la dépendance temporelle des données.

### Question 5
