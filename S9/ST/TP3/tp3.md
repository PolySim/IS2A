# TP3

## PT 1

### Question 1

```python
import matplotlib.pyplot as plt
import numpy as np


def load_data(filename="AirPassengers.txt"):
    with open(filename, "r") as f:
        data = f.readlines()
        res = np.array([int(line.strip()) for line in data])
        f.close()
        return np.log(res)


def create_range_date(start, end):
    return np.arange(start, end, dtype="datetime64[M]")


def display_basic_data(data):
    plt.plot(create_range_date("1949-01", "1961-01"), data, label="Nombre passager")
    plt.xlabel("Date")
    plt.ylabel("Nombre passager")
    plt.title("Nombre passager en fonction du temps")
    plt.legend()
    plt.show()
```

Une série stationnaire est une série dont la moyenne et la variance sont constantes au cours du temps. Ici la série a une tendance à la hausse et une saisonnalité annuelle. Cela montre que la série n'est pas stationnaire.

Pour voir la saisonnalité et la tendance de la série :

```python
from statsmodels.tsa.seasonal import seasonal_decompose

def display_saisonial_trend(data):
    decomposition = seasonal_decompose(data, model="multiplicative", period=12)
    plt.plot(decomposition.seasonal, label="Seasonal")
    plt.show()
    plt.plot(decomposition.trend, label="Trend")
    plt.legend()
    plt.show()
```

### Question 2

```python
def estime_params(data):
    t = np.arange(len(data))
    a, b = np.polyfit(t, data, 1)
    return a, b


def tendance_lineaire(a, b):
    x = [a * i + b for i in range(len(dates))]
    return x


def display_tendance_lineaire(a, b):
    plt.plot(dates, data, label="Nombre passager")
    plt.xlabel("Date")
    plt.ylabel("Nombre passager")
    plt.title("Nombre passager en fonction du temps")
    plt.plot(dates, tendance_lineaire(a, b), label="Tendance linéaire")
    plt.legend()
    plt.show()
```

### Question 3

```py
def remove_tendance_lineaire(data, a, b):
    tendance = tendance_lineaire(a, b)
    return data - tendance

def calc_mean_residu(residu): # residu = remove_tendance_lineaire(data, a, b)
    return np.mean(residu)
```

### Question 4

```python
from statsmodels.graphics.tsaplots import plot_acf

def auto_correlation(data):
    plot_acf(data, lags=40)
```

## PT 2

### Question 1

```python
def remove_with_dif(data):
    serie = pd.Series(data, index=dates)
    serie_diff = serie.diff(1).diff(12)
    return serie_diff[13:]


def remove_with_dif_np(data):
    diff_tendance = np.diff(data, n=1)
    diff_complete = diff_tendance[12:] - diff_tendance[:-12]
    return diff_complete


def display_diff(serie_diff):
    serie_diff.plot()
    plt.axhline(0, linestyle="--")
    plt.title("Série différenciée")
    plt.show()
```

### Question 2

Après différenciation, la série semble nettement plus stationnaire : la tendance et la saisonnalité ont disparu et la série oscille autour de zéro. Cependant, l’amplitude des fluctuations semble augmenter vers la fin de la série, ce qui suggère que la variance n’est pas parfaitement constante. La stationnarité n’est donc pas totalement évidente graphiquement.

## PT 3

### Question 1

```python
def create_series(data):
    serie = pd.Series(data=np.array(data, dtype=float), index=pd.to_datetime(dates))
    return serie


def calc_mean_mobile(serie):
    return serie.rolling(window=12, center=False).mean()


def calc_tendant_from_mean_mobile(mean_mobile):
    return mean_mobile.rolling(window=2, center=True).mean().shift(-1)


def display_tendance_mean_mobile(data):
    serie = create_series(data)
    mean_mobile = calc_mean_mobile(serie)
    tendance = calc_tendant_from_mean_mobile(mean_mobile)

    plt.figure(figsize=(12, 6))

    plt.plot(serie.index, serie.values, label="Série originale")

    plt.plot(tendance.index, tendance.values, label="Tendance - moyenne mobile")

    plt.xlabel("Date")
    plt.ylabel("Nombre de passagers")
    plt.title("Estimation de la tendance par moyenne mobile")
    plt.legend()
    plt.show()


def remove_tendance_mean_mobile(serie, tendance):
    return serie / tendance


def calc_saisonnalite(serie_without_trend):
    df = pd.DataFrame({"sans_tendance": serie_without_trend})

    # Récupération du numéro du mois : 1 = janvier, ..., 12 = décembre
    df["mois"] = df.index.month

    # Moyenne pour chaque mois
    coefficients_saisonniers = df.groupby("mois")["sans_tendance"].mean()

    # Normalisation pour que la moyenne des coefficients soit égale à 1
    coefficients_saisonniers = (
        coefficients_saisonniers / coefficients_saisonniers.mean()
    )

    return coefficients_saisonniers


def create_saisonnalite(serie_without_trend, coefficients_saisonniers):
    saisonnalite = pd.Series(
        [
            coefficients_saisonniers.loc[date.month]
            for date in serie_without_trend.index
        ],
        index=serie_without_trend.index,
    )
    return saisonnalite


def remove_saisonnalite_and_trend(serie, tendance, saisonnalite):
    return serie / (tendance * saisonnalite)


def display_series_without_trend_and_saisonnalite(residus):
    plt.figure(figsize=(12, 6))

    plt.plot(residus.index, residus.values, label="Résidus")

    plt.axhline(y=1, linestyle="--", label="Moyenne théorique = 1")

    plt.xlabel("Date")
    plt.ylabel("Résidus")
    plt.title("Série après suppression de la tendance et de la saisonnalité")
    plt.legend()
    plt.show()


def display_saisonnalite(coefficients_saisonniers):
    plt.figure(figsize=(10, 5))

    plt.plot(range(1, 13), coefficients_saisonniers.values, marker="o")

    plt.axhline(y=1, linestyle="--")

    plt.xticks(range(1, 13))
    plt.xlabel("Mois")
    plt.ylabel("Coefficient saisonnier")
    plt.title("Coefficients saisonniers")
    plt.show()

serie = create_series(data)
mean_mobile = calc_mean_mobile(serie)
serie_without_tendance = remove_tendance_mean_mobile(serie, mean_mobile)
coef_saisonniers = calc_saisonnalite(serie_without_tendance)
saisonnalite = create_saisonnalite(serie_without_tendance, coef_saisonniers)
residus = remove_saisonnalite_and_trend(serie, mean_mobile, saisonnalite)
display_series_without_trend_and_saisonnalite(residus)
display_saisonnalite(coef_saisonniers)
```

### Question 2

Après suppression de la tendance par moyenne mobile et de la composante saisonnière, la série obtenue semble stationnaire. Elle oscille autour d’une valeur moyenne constante, proche de 1 dans le cadre du modèle multiplicatif, et ne présente plus de tendance ni de saisonnalité apparente. La variance semble également plus stable au cours du temps.

Il faut refaire tout de même l'auto corrélation

```python
plot_acf(residus.dropna(), lags=40)
```
