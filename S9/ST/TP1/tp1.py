import matplotlib.pyplot as plt
import numpy as np


def load_data(file="varicelle.txt"):
    with open(file, "r") as f:
        data = f.read()
        res = np.array([], dtype=np.int32)
        for line in data.splitlines()[1:]:
            res = np.append(res, np.array(line.split(), dtype=np.int32))
        f.close()
    return res


global_data = load_data()


def display_series(data=global_data):
    dates = np.arange("1931-01", "1972-07", dtype="datetime64[M]")
    serie = {"dates": dates, "cas": data}

    plt.figure()
    plt.plot(serie["dates"], serie["cas"])
    plt.title("Cas mensuels de varicelle à New York")
    plt.xlabel("Année")
    plt.ylabel("Nombre de cas")


def calc_mean(data=global_data):
    return np.mean(data)


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


##### PT 2


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


def display_autocorr(epsilon):
    auto_corr(epsilon)
    plt.title("Question 3 : autocorrélation du bruit blanc")


def simule_st_1():
    n = 100
    epsilon = calc_espilon(n)
    t = np.arange(1, n + 1)
    return (0.5 * t + 2 * epsilon, t)


def display_st(x, t):
    plt.figure()
    plt.plot(t, x)
    plt.legend()
    plt.title("tendance + bruit")
    plt.xlabel("Temps")
    plt.ylabel("X(t)")


def simule_st_2():
    n = 100
    epsilon = calc_espilon(n)
    t = np.arange(1, n + 1)
    return (epsilon + 3 * np.cos(t * np.pi / 6), t)


def simule_st_3():
    n = 100
    epsilon = calc_espilon(n)
    t = np.arange(1, n + 1)
    return (0.5 * t + epsilon + 3 * np.cos(t * np.pi / 6), t)


if __name__ == "__main__":
    # display_series()
    # calc_mean()
    # auto_corr()
    # display_tendance()
    # evo_annuelle()
    #
    # simule_bruit_blanc()
    # display_autocorr(calc_espilon(10000))
    (x, t) = simule_st_1()
    display_st(x, t)
    auto_corr(x)
    plt.title("Autocorrélation 1 avec tendance")

    (x2, t2) = simule_st_2()
    display_st(x2, t2)
    auto_corr(x2)
    plt.title("Autocorrélation 2 avec tendance")

    (x3, t3) = simule_st_3()
    display_st(x3, t3)
    auto_corr(x3)
    plt.title("Autocorrélation 3 avec tendance")

    plt.show()
