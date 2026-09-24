"""Calculs et figures de la suite du TP, avec l'environnement ../venv.

Exécution : ../venv/bin/python tp4_suite.py
Les résultats sont écrits dans resultats_tp4, sans modifier tp4.py ni tp4.md.
"""

from pathlib import Path
import json
import os
import tempfile

os.environ.setdefault("MPLCONFIGDIR", str(Path(tempfile.gettempdir()) / "tp4-mpl"))
import matplotlib

matplotlib.use("Agg")
import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
import statsmodels
from statsmodels.graphics.tsaplots import plot_acf, plot_pacf
from statsmodels.stats.diagnostic import acorr_ljungbox
from statsmodels.stats.stattools import jarque_bera
from statsmodels.tsa.arima.model import ARIMA
from statsmodels.tsa.stattools import acf, adfuller, kpss

ROOT = Path(__file__).resolve().parent
OUT = ROOT / "resultats_tp4"


def load(name):
    values = np.loadtxt(ROOT / "data" / f"{name}.txt")
    assert values.ndim == 1 and np.isfinite(values).all()
    return values


def save(fig, name):
    fig.tight_layout()
    fig.savefig(OUT / f"{name}.png", dpi=150)
    plt.close(fig)


def correlations(values, title, name):
    fig, axes = plt.subplots(1, 2, figsize=(12, 4))
    plot_acf(values, lags=40, ax=axes[0], zero=False)
    plot_pacf(values, lags=40, ax=axes[1], zero=False, method="ywm")
    axes[0].set_title(f"{title} — ACF")
    axes[1].set_title(f"{title} — PACF")
    for ax in axes:
        ax.set_xlabel("Retard")
    save(fig, name)


def fit(values, order, **kwargs):
    result = ARIMA(values, order=order, **kwargs).fit(method_kwargs={"maxiter": 500})
    if not result.mle_retvals["converged"]:
        raise RuntimeError(f"Non-convergence pour {order}, {kwargs}")
    return result


def diagnostics(result, name, model_df, skip=0, lags=(10, 20, 30)):
    residuals = np.asarray(result.resid)[skip:]
    fig, axes = plt.subplots(1, 2, figsize=(12, 4))
    axes[0].plot(np.arange(skip + 1, skip + 1 + len(residuals)), residuals, lw=0.8)
    axes[0].axhline(0, color="red", ls="--", lw=1)
    axes[0].set_title(f"{name} — résidus")
    axes[0].set_xlabel("Observation")
    plot_acf(residuals, lags=40, ax=axes[1], zero=False)
    axes[1].set_title("ACF des résidus")
    save(fig, f"{name}_residus")
    lb = acorr_ljungbox(residuals, lags=list(lags), model_df=model_df)
    return {
        "params": dict(zip(result.param_names, result.params.tolist())),
        "aic": float(result.aic), "bic": float(result.bic),
        "lb": {str(k): float(v) for k, v in lb.lb_pvalue.items()},
        "jb_pvalue": float(jarque_bera(residuals)[1]),
        "resid_mean": float(residuals.mean()),
        "ar_root_moduli": np.abs(result.arroots).tolist(),
        "ma_root_moduli": np.abs(result.maroots).tolist(),
    }


def forecast_frame(result, steps, index):
    forecast = result.get_forecast(steps=steps)
    interval = np.asarray(forecast.conf_int(alpha=0.05))
    frame = pd.DataFrame({"prevision": np.asarray(forecast.predicted_mean),
                          "borne_basse_95": interval[:, 0],
                          "borne_haute_95": interval[:, 1]}, index=index)
    assert len(frame) == steps and np.isfinite(frame.values).all()
    assert (frame.borne_basse_95 <= frame.prevision).all()
    assert (frame.prevision <= frame.borne_haute_95).all()
    return frame


def main():
    OUT.mkdir(exist_ok=True)
    info = {"statsmodels_version": statsmodels.__version__}
    ma, arma, series, rain = [load(n) for n in ["MA", "ARMA", "Exercice2", "SanFransisco"]]
    assert len(series) == 300 and len(rain) == 35 * 12

    correlations(ma, "MA.txt", "ma_correlations")
    correlations(arma, "ARMA.txt", "arma_correlations")
    info["ma"] = diagnostics(fit(ma, (0, 0, 3)), "ma", 3)
    arma_orders = [(1, 1), (1, 2), (2, 1), (2, 2), (2, 3), (3, 3), (2, 4)]
    arma_fits = {(p, q): fit(arma, (p, 0, q)) for p, q in arma_orders}
    info["arma_comparison"] = [dict(p=p, q=q, aic=r.aic, bic=r.bic)
                               for (p, q), r in arma_fits.items()]
    info["arma"] = diagnostics(arma_fits[2, 3], "arma", 5)

    differences = np.diff(series)
    info["stationarity"] = {}
    for label, values in [("originale", series), ("difference", differences)]:
        a = adfuller(values, regression="c", autolag="AIC")
        k = kpss(values, regression="c", nlags="auto")
        info["stationarity"][label] = {"adf_p": float(a[1]), "kpss_p_bound": float(k[1])}
    info["adf_trend_p"] = float(adfuller(series, regression="ct", autolag="AIC")[1])
    fig, axes = plt.subplots(2, 1, figsize=(11, 6))
    axes[0].plot(np.arange(1, 301), series)
    axes[0].set_title("Exercice2.txt — série initiale")
    axes[1].plot(np.arange(2, 301), differences, lw=0.8)
    axes[1].axhline(differences.mean(), color="red", ls="--", label="Moyenne des différences")
    axes[1].legend()
    axes[1].set_title("Première différence")
    axes[1].set_xlabel("Temps")
    save(fig, "exercice2_series")
    correlations(series, "Série initiale", "exercice2_correlations_initiales")
    correlations(differences, "Première différence", "exercice2_correlations")
    orders = [(1, 0), (2, 0), (0, 1), (0, 2), (1, 1), (2, 1), (1, 2), (2, 2), (2, 4)]
    fits = {(p, q): fit(series, (p, 1, q), trend="t") for p, q in orders}
    info["prediction_comparison"] = [dict(p=p, q=q, aic=r.aic, bic=r.bic)
                                     for (p, q), r in fits.items()]
    chosen = fits[2, 2]
    info["prediction"] = diagnostics(chosen, "exercice2", 4, skip=1)
    future = forecast_frame(chosen, 20, pd.Index(range(301, 321), name="temps"))
    future.to_csv(OUT / "previsions_exercice2.csv")
    fig, ax = plt.subplots(figsize=(11, 4))
    ax.plot(np.arange(221, 301), series[-80:], label="Observations")
    ax.plot(future.index, future.prevision, color="tab:orange", label="Prévisions")
    ax.fill_between(future.index, future.borne_basse_95, future.borne_haute_95,
                    color="tab:orange", alpha=0.2, label="Intervalle de prévision à 95 %")
    ax.axvline(300, color="grey", ls="--")
    ax.set_title("Exercice 2 — prévisions pour les 20 temps suivants")
    ax.set_xlabel("Temps")
    ax.legend()
    save(fig, "exercice2_previsions")

    dates = pd.date_range("1932-01-01", periods=420, freq="MS")
    fig, axes = plt.subplots(2, 1, figsize=(11, 6))
    axes[0].plot(dates, rain, lw=0.8)
    axes[0].set_title("Précipitations mensuelles — 1932–1966")
    axes[0].set_ylabel("Unités du fichier")
    monthly = rain.reshape(-1, 12).mean(axis=0)
    axes[1].bar(np.arange(1, 13), monthly)
    axes[1].set_xticks(np.arange(1, 13))
    axes[1].set_xlabel("Mois (janvier = 1)")
    axes[1].set_ylabel("Moyenne")
    save(fig, "pluie_series")
    correlations(rain, "Précipitations", "pluie_correlations")
    info["rain_monthly_means"] = monthly.tolist()
    info["rain_acf"] = acf(rain, nlags=36)[[12, 24, 36]].tolist()
    full = fit(rain, (0, 0, 0), seasonal_order=(2, 0, 0, 12), trend="c")
    info["rain_full"] = diagnostics(full, "pluie", 2, skip=24, lags=(12, 24, 36))
    train, actual = rain[:384], rain[384:]
    assert dates[383].year == 1963 and dates[384].year == 1964
    training = fit(train, (0, 0, 0), seasonal_order=(2, 0, 0, 12), trend="c")
    info["rain_train"] = diagnostics(training, "pluie_apprentissage", 2, skip=24, lags=(12, 24, 36))
    forecast = forecast_frame(training, 36, pd.Index(dates[384:], name="date"))
    forecast["observation"] = actual
    forecast.to_csv(OUT / "previsions_precipitations.csv")
    info["rain_errors"] = {
        "rmse": float(np.sqrt(np.mean((forecast.prevision.values - actual) ** 2))),
        "mae": float(np.mean(np.abs(forecast.prevision.values - actual))),
        "seasonal_naive_rmse": float(np.sqrt(np.mean((np.tile(train[-12:], 3) - actual) ** 2))),
    }
    fig, ax = plt.subplots(figsize=(12, 5))
    ax.plot(dates[360:], rain[360:], label="Observations", lw=1.2)
    ax.plot(forecast.index, forecast.prevision, label="Prévisions depuis fin 1963", color="tab:orange")
    ax.fill_between(forecast.index, forecast.borne_basse_95, forecast.borne_haute_95,
                    color="tab:orange", alpha=0.2, label="Intervalle de prévision à 95 %")
    ax.axvline(dates[384], color="grey", ls="--")
    ax.set_title("Précipitations — prévisions de janvier 1964 à décembre 1966")
    ax.set_ylabel("Unités du fichier")
    ax.legend()
    save(fig, "pluie_previsions")
    (OUT / "resultats.json").write_text(json.dumps(info, ensure_ascii=False, indent=2) + "\n")
    print(f"Calculs terminés : {OUT}")


if __name__ == "__main__":
    main()
