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


data = load_data("data/AR.txt")
auto_correlation(data)
plt.show()
```

En regardant l'autocorrélation on observe qu'elle diminue vers `0` ce qui fait bien penser à
un processus `AR`.
En regardant l'autocorrélation partielle on observe deux pics significatifs aux retards 1 et 2, puis des valeurs globalement proches de zéro, ce qui fait bien penser à
un processus `AR(2)`.

### Question 2

```py
from statsmodels.tsa.arima.model import ARIMA

def estime_params(data, order):
    model = ARIMA(data, order=order)
    results = model.fit()
    return results

results = estime_params(data, order=(2, 0, 0))
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

Le paramètre `order=(p, d, q)` définit les ordres du modèle. Ici, on utilise
`order=(2, 0, 0)` :

- `p=2` : deux retards de la série dans la partie AR, soit $X_{t-1}$ et $X_{t-2}$.
- `d=0` : aucune différenciation appliquée à la série.
- `q=0` : aucun retard du bruit dans une partie MA.

Un ARIMA(2, 0, 0) est donc un AR(2). La fonction `estime_params` pourra aussi
servir pour les modèles MA et ARMA.

**Explication des résultats**

| Paramètre | Interprétation | Estimation |
|---|---|---:|
| `const` | Espérance $\mu$ de la série dans cette paramétrisation | 0,0427 |
| `sigma2` | Variance du bruit $\sigma_\varepsilon^2$ | 1,0328 |
| `ar.L1` | $\phi_1$, coefficient du retard 1 | 1,0014 |
| `ar.L2` | $\phi_2$, coefficient du retard 2 | −0,2555 |

Avec la convention de [`ARIMA` dans statsmodels](https://www.statsmodels.org/stable/generated/statsmodels.tsa.arima.model.ARIMA.html),
le modèle s'écrit sous forme centrée :

$$
X_t-\mu=\phi_1(X_{t-1}-\mu)+\phi_2(X_{t-2}-\mu)+\varepsilon_t.
$$

La constante additive de l'équation non centrée vaut donc
$c=\mu(1-\phi_1-\phi_2)\approx 0{,}01085$. Le modèle estimé est :

$$
X_t\approx 0{,}01085+1{,}0014X_{t-1}-0{,}2555X_{t-2}+\varepsilon_t.
$$

Les deux coefficients AR sont significativement différents de zéro : leurs
intervalles de confiance à 95 % ne contiennent pas zéro. Ils sont proches des
coefficients de simulation $1$ et $-1/4$. La moyenne estimée est compatible
avec zéro (p-valeur de 0,811).

### Question 3

```python
def calcule_racine(phi1, phi2):
    return np.roots([-phi2, -phi1, 1])


def is_stationary():
    return np.all(np.abs(calcule_racine(1, -1 / 4)) > 1)
```

`np.roots` attend les coefficients du polynôme par puissances décroissantes.
Pour les coefficients de simulation $\phi_1=1$ et $\phi_2=-1/4$, le polynôme
autorégressif est :

$$
P(z)=1-\phi_1z-\phi_2z^2=1-z+\frac14z^2
=\left(1-\frac z2\right)^2.
$$

Il possède une racine double égale à $2$. Les racines ont donc un module
strictement supérieur à $1$ : le processus AR(2) est stationnaire et la
fonction `is_stationary()` renvoie `True` pour ces coefficients.

Le fait que $\phi_1=1$ ne suffit pas à conclure à une non-stationnarité : il
faut tenir compte des deux coefficients ensemble.

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


display_residus(results.resid)
plt.show()
```

- Les résidus fluctuent autour de zéro, sans tendance ni périodicité visible.
- Leur dispersion semble relativement constante au cours du temps.
- L’ACF est proche de zéro à presque tous les retards, sans structure
  persistante.
- Un pic dépasse légèrement la bande de confiance vers le retard 8. Un
  dépassement isolé parmi 40 retards n’est pas inhabituel avec des bandes à 95
  % et ne suffit pas à remettre en cause le modèle.

Les résidus sont visuellement centrés autour de zéro, de variance approximativement constante et ne présentent pas d’autocorrélation notable persistante. Ils sont donc compatibles avec un bruit blanc. La modélisation AR(2) semble ainsi bien rendre compte de la dépendance temporelle des données.

Les diagnostics du résumé sont également favorables : le test de Ljung–Box
au retard 1 donne une p-valeur de 0,84 et ne détecte pas d'autocorrélation à
ce retard. Le test d'hétéroscédasticité (p-valeur de 0,23) ne détecte pas de
variation de variance. Le test de Jarque–Bera (p-valeur de 0,23) ne rejette
pas la normalité, qui n'est toutefois pas nécessaire pour un bruit blanc.
Le Ljung–Box affiché porte seulement sur le retard 1 et ne valide donc pas,
à lui seul, l'absence d'autocorrélation à tous les retards.

### Question 5

On reprend les mêmes étapes pour `MA.txt` et `ARMA.txt`, en réutilisant les
fonctions définies précédemment. L'identification et l'estimation du modèle
MA ci-dessous ont été calculées sur les 500 observations de `MA.txt`.
Les résultats numériques du modèle ARMA et les conclusions sur les résidus
de ces deux modèles restent à compléter.

#### Observation des autocorrélations

```python
data_ma = load_data("data/MA.txt")
data_arma = load_data("data/ARMA.txt")

auto_correlation(data_ma)
plt.gcf().suptitle("Processus MA")
plt.tight_layout(rect=(0, 0, 1, 0.92))
plt.show()

auto_correlation(data_arma)
plt.gcf().suptitle("Processus ARMA")
plt.tight_layout(rect=(0, 0, 1, 0.92))
plt.show()
```

| Type de processus | ACF | PACF |
|---|---|---|
| AR($p$) | Décroissance progressive | Coupure après le retard $p$ |
| MA($q$) | Coupure après le retard $q$ | Décroissance progressive |
| ARMA($p,q$) | Généralement pas de coupure nette | Généralement pas de coupure nette |

Ces propriétés concernent les corrélations théoriques : sur les graphiques
empiriques, les valeurs après une coupure restent globalement proches de
zéro, avec des fluctuations d'échantillonnage.

Pour `MA.txt`, les premières autocorrélations empiriques sont :

| Retard | ACF |
|---|---:|
| 1 | 0,7001 |
| 2 | 0,3550 |
| 3 | 0,1174 |
| 4 | −0,0439 |
| 5 | −0,0443 |
| 6 | −0,0303 |

L'ACF diminue fortement sur les trois premiers retards, puis fluctue autour
de zéro. La PACF présente encore plusieurs pics à des retards supérieurs.
Le troisième retard de l'ACF reste toutefois dans la bande à 95 % affichée
par défaut par `plot_acf` : le graphique seul ne distingue donc pas
nettement MA(2) de MA(3). On complète cette lecture par une comparaison des
critères d'information, qui conduit à retenir **MA(3)**.

Pour `ARMA.txt`, les ordres sont plus difficiles à identifier graphiquement ;
on compare plusieurs petits modèles.

#### Estimation des coefficients

Pour le processus MA, on utilise `order=(0, 0, q)`. Les ajustements des
modèles MA d'ordres 0 à 5 sur `MA.txt` donnent les valeurs suivantes ; tous
ont convergé :

| Ordre $q$ | AIC | BIC |
|---|---:|---:|
| 0 | 1831,980 | 1840,409 |
| 1 | 1510,877 | 1523,520 |
| 2 | 1491,120 | 1507,979 |
| **3** | **1423,947** | **1445,020** |
| 4 | 1424,650 | 1449,938 |
| 5 | 1426,344 | 1455,846 |

Les deux critères sont minimaux pour **$q=3$** parmi les ordres comparés.
L'AIC du MA(4) est proche, mais sa complexité supplémentaire n'améliore
aucun des deux critères. On retient donc `order=(0, 0, 3)`.

```python
q = 3
results_ma = estime_params(data_ma, order=(0, 0, q))
print(results_ma.summary())
```

Les paramètres estimés sont :

| Paramètre | Estimation |
|---|---:|
| `const` ($\mu$) | −0,0723 |
| `ma.L1` ($\theta_1$) | 0,9737 |
| `ma.L2` ($\theta_2$) | 0,5310 |
| `ma.L3` ($\theta_3$) | 0,4061 |
| `sigma2` ($\sigma_\varepsilon^2$) | 0,9870 |

Le modèle estimé s'écrit donc :

$$
X_t\approx -0{,}0723+\varepsilon_t+0{,}9737\varepsilon_{t-1}
+0{,}5310\varepsilon_{t-2}+0{,}4061\varepsilon_{t-3}.
$$

Les coefficients `ma.L1`, `ma.L2`, etc. sont les coefficients des bruits
passés dans le modèle :

$$
X_t-\mu=\varepsilon_t+\theta_1\varepsilon_{t-1}
+\cdots+\theta_q\varepsilon_{t-q}.
$$

Pour le processus ARMA, on compare les candidats suivants sur la même série,
sans différenciation :

```python
ordres = [(1, 0, 1), (1, 0, 2), (2, 0, 1), (2, 0, 2)]
modeles = {}

for ordre in ordres:
    resultat = estime_params(data_arma, order=ordre)
    modeles[ordre] = resultat
    print(f"{ordre} : AIC={resultat.aic:.2f}, BIC={resultat.bic:.2f}")

meilleur_ordre = min(modeles, key=lambda ordre: modeles[ordre].aic)
results_arma = modeles[meilleur_ordre]

print("Ordre retenu selon l'AIC :", meilleur_ordre)
print(results_arma.summary())
```

Un AIC plus faible est préférable parmi les modèles comparés. Le BIC est
également affiché et pénalise davantage le nombre de paramètres pour cet
échantillon. Le choix fait ici selon l'AIC reste à valider par l'analyse des
résidus et suppose que les ajustements ont convergé.

Le modèle ARMA combine les coefficients `ar.L1`, etc. et `ma.L1`, etc. :

$$
X_t-\mu=\sum_{i=1}^{p}\phi_i(X_{t-i}-\mu)
+\varepsilon_t+\sum_{j=1}^{q}\theta_j\varepsilon_{t-j}.
$$

Comme pour l'AR, `const` représente ici la moyenne du processus et `sigma2`
la variance du bruit.

#### Stationnarité

Un MA d'ordre fini est stationnaire lorsque les innovations forment un bruit
blanc de variance finie, quelles que soient les valeurs de ses coefficients.

Pour un ARMA sous sa forme causale, on vérifie que les racines de son
polynôme AR $1-\phi_1z-\cdots-\phi_pz^p$ ont toutes un module strictement
supérieur à 1 :

```python
# Coefficients du polynôme par puissances décroissantes.
racines_ar = np.roots(np.r_[-results_arma.arparams[::-1], 1])

print("Racines AR :", racines_ar)
print("Stationnaire :", np.all(np.abs(racines_ar) > 1))
```

Par défaut, `ARIMA` impose la stationnarité de la partie AR pendant
l'estimation. Ce contrôle concerne donc le modèle ajusté, et ne prouve pas
à lui seul la stationnarité de la série observée.

#### Analyse des résidus

```python
for nom, resultat in [("MA", results_ma), ("ARMA", results_arma)]:
    display_residus(resultat.resid)
    plt.gcf().suptitle(f"Diagnostic du modèle {nom}")
    plt.tight_layout(rect=(0, 0, 1, 0.92))
    plt.show()
```

Pour chaque modèle, on vérifie que les résidus fluctuent autour de zéro,
que leur dispersion reste relativement constante et que leur ACF ne montre
pas de structure persistante. Quelques dépassements isolés de la bande de
confiance ne suffisent pas à invalider un modèle.

Si ces conditions sont satisfaites, les résidus sont compatibles avec un
bruit blanc et la modélisation semble fidèle à la dépendance temporelle des
données. Si des autocorrélations structurées persistent, il faut reconsidérer
les ordres retenus, même si le modèle possède le plus petit AIC parmi les
candidats comparés.
