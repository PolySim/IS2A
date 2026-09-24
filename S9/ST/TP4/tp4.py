import matplotlib.pyplot as plt
import numpy as np
from statsmodels.graphics.tsaplots import plot_acf, plot_pacf
from statsmodels.tsa.arima.model import ARIMA


def load_data(filename="data/MA.txt"):
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


def estime_params_AR(data, order):
    model = ARIMA(data, order=order)
    results = model.fit()
    return results


def calcule_racine(phi1, phi2):
    return np.roots([-phi2, -phi1, 1])


def is_stationary():
    return np.all(np.abs(calcule_racine(1, -1 / 4)) > 1)


def display_residus(residus):
    _fig, axes = plt.subplots(1, 2, figsize=(12, 4))

    axes[0].plot(residus)
    axes[0].axhline(0, color="red", linestyle="--")
    axes[0].set_title("Résidus")

    plot_acf(residus, lags=40, ax=axes[1], zero=False)
    axes[1].set_title("ACF des résidus")

    plt.tight_layout()


if __name__ == "__main__":
    data = load_data()
    auto_correlation(data)
    # results = estime_params_AR(data, order=(2, 0, 0))
    # print(results.summary())
    # residus = results.resid
    # display_residus(residus)

    plt.show()
