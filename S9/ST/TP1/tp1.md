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
