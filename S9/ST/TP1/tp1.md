# TP 1

## PT 1

### Q2

```python
def load_data(file="varicelle.txt"):
    with open(file, "r") as f:
        data = f.read()
        res = np.array([], dtype=np.int32)
        for line in data.splitlines()[1:]:
            res = np.append(res, np.array(line.split(), dtype=np.int32))
        f.close()
    return res


def display_series(data):
    dates = np.arange("1931-01", "1972-07", dtype="datetime64[M]")
    serie = {"dates": dates, "cas": data}

    plt.figure()
    plt.plot(serie["dates"], serie["cas"])
    plt.title("Cas mensuels de varicelle à New York")
    plt.xlabel("Année")
    plt.ylabel("Nombre de cas")
```

On observe une saisonnalité de 12 mois et une baisse globale du nombre de cas. \
Les pics sont au printemps et les creux en fin d'été.

### Q3

```python
def calc_mean(data):
    return np.mean(data)
# 732.4076305220883
```

### Q4

```python
def auto_corr(data=global_data):
    centres = data - calc_mean(data)
    acf = np.array([1], dtype=np.float64)
    for retard in range(1, 26):
        acf = np.append(
            acf, np.sum(centres[retard:] * centres[:-retard]) / np.sum(centres**2)
        )

    seuil = 1.96 / np.sqrt(len(data))
    plt.figure()
    plt.stem(range(26), acf)
    plt.axhline(seuil, color="red", linestyle="--")
    plt.axhline(-seuil, color="red", linestyle="--")
    plt.title("Autocorrélations (retards de 0 à 25 mois)")
    plt.xlabel("Retard en mois")
    plt.ylabel("Autocorrélation")
```

Les pics à 12 et 24 mois confirment la saisonnalité annuelle. Les corrélations négatives vers 6 et 18 mois opposent les saisons.
Les pointillés sont les seuils approximatifs à 95 % pour chaque autocorrélation sous l'hypothèse d'un bruit blanc.

### Q5

```python
def display_tendance(data=global_data):
    for i, annee in enumerate(range(1931, 1973)):
        valeurs = data[i * 12 : (i + 1) * 12]
        plt.plot(range(1, len(valeurs) + 1), valeurs, label=str(annee))

    plt.title("Évolution mensuelle par année")
    plt.xlabel("Mois")
    plt.ylabel("Nombre de cas")
    plt.xticks(range(1, 13))
    plt.legend(ncol=3, fontsize=7, bbox_to_anchor=(1, 1), loc="upper left")
    plt.tight_layout()
```

### Q6

```python
def evo_annuelle(data=global_data):
    evolution = []
    for i, _annee in enumerate(range(1931, 1973)):
        valeurs = data[i * 12 : (i + 1) * 12]
        evolution.append(np.sum(valeurs))

    plt.figure()
    plt.plot(range(1931, 1973), evolution, marker=".")
    plt.title("Évolution annuelle")
    plt.xlabel("Année")
    plt.ylabel("Nombre de cas")
```

### Q7

Les profils mensuels confirment le cycle saisonnier.
Les totaux annuels montrent une baisse surtout à partir de la fin des années 1950, avec des variations importantes entre les années.

## TP2

### Q1

L'autocorrélation compare une série à elle-même, décalée dans le temps. Au retard 0, elle vaut 1 : on compare chaque valeur à elle-même. Aux autres retards, elle vaut 0 en théorie : les tirages sont indépendants.

### Q2

```python
def calc_espilon(n):
    epsilon = np.random.normal(0, 1, n)
    return epsilon


def simule_bruit_blanc(n=100):
    t = np.arange(1, n + 1)
    epsilon = calc_espilon(n)

    plt.figure()
    plt.plot(t, epsilon)
    plt.title("Bruit blanc gaussien")
    plt.xlabel("Temps")
    plt.ylabel("epsilon")

```

### Q3

```python
def display_autocorr(epsilon):
    auto_corr(epsilon)
    plt.title("Question 3 : autocorrélation du bruit blanc")
```

Avec seulement 100 valeurs, les autocorrélations ne sont pas exactement 0 mais bien dans la zone de confiance.

### Q4

Avec 1000 valeurs, les autocorrélations sont généralement plus proches de 0. Plus le nombre de valeurs augmente, plus les autocorrélations se rapprochent de 0.

### Q5

```python
def simule_st():
    n = 100
    epsilon = calc_espilon(n)
    t = np.arange(1, n + 1)
    return (0.5 * t + 2 * epsilon, t)
```

### Q6

```python
def display_st():
    (x, t) = simule_st()
    plt.figure()
    plt.plot(t, x)
    plt.plot(t, 0.5 * t, "--", label="Tendance : 0.5t")
    plt.legend()
    plt.title("tendance + bruit")
    plt.xlabel("Temps")
    plt.ylabel("X(t)")
```

La droite 0.5t monte avec le temps ; le bruit fait osciller la série autour.

### Q7

Les autocorrélations restent positives aux petits retards et diminuent lentement. Cela vient de la tendance commune aux valeurs successives.

### Q8

```python
def simule_st_2():
    n = 100
    epsilon = calc_espilon(n)
    t = np.arange(1, n + 1)
    return (epsilon + 3 * np.cos(t * np.pi / 6), t)
```

Le cosinus se répète tous les 12 pas de temps.
L'autocorrélation est positive vers 12 et 24 : le cycle se retrouve.
Elle est négative vers 6 et 18 : les sommets correspondent aux creux.

### Q9

```python
def simule_st_3():
    n = 100
    epsilon = calc_espilon(n)
    t = np.arange(1, n + 1)
    return (0.5 * t + epsilon + 3 * np.cos(t * np.pi / 6), t)
```

La série monte tout en oscillant selon un cycle de 12 pas.
La tendance domine l'autocorrélation ; le cycle y est moins visible.
