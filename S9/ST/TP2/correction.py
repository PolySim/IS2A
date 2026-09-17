"""Correction du TP2 : lissage exponentiel et prévision.

Exécution depuis le dossier du TP : python3 correction.py
Dépendances : numpy et matplotlib. Les récurrences sont implémentées ici.

Le lissage double retenu est celui de Brown : deux lissages successifs avec
le même alpha, conformément à la comparaison d'un unique paramètre demandée.
Holt-Winters utilise une tendance additive et une saisonnalité additive ou
multiplicative de période 12 (cos(t*pi/6) et mesures mensuelles du CO2).
"""

import matplotlib.pyplot as plt
import numpy as np

ALPHAS = (0.1, 0.3, 0.5, 0.7, 0.9)
PERIODE = 12


def sce(observations, previsions):
    """Somme des carrés des erreurs, sans division par l'effectif."""
    return float(np.sum((np.asarray(observations) - previsions) ** 2))


def lissage_simple(y, alpha, horizon):
    """l_t = alpha*y_t + (1-alpha)*l_(t-1), prévision constante."""
    niveau = float(y[0])
    ajustes = np.full(len(y), np.nan)
    for i in range(1, len(y)):
        ajustes[i] = niveau  # Prévision à un pas, avant d'observer y[i].
        niveau = alpha * y[i] + (1 - alpha) * niveau
    return ajustes, np.full(horizon, niveau)


def lissage_double(y, alpha, horizon):
    """Brown : a_t=2*s1_t-s2_t ; b_t=alpha/(1-alpha)*(s1_t-s2_t)."""
    s1 = s2 = float(y[0])
    niveau, pente = s1, 0.0
    ajustes = np.full(len(y), np.nan)
    for i in range(1, len(y)):
        ajustes[i] = niveau + pente
        s1 = alpha * y[i] + (1 - alpha) * s1
        s2 = alpha * s1 + (1 - alpha) * s2
        niveau = 2 * s1 - s2
        pente = alpha / (1 - alpha) * (s1 - s2)
    return ajustes, niveau + pente * np.arange(1, horizon + 1)


def holt_winters(y, alpha, beta, gamma, horizon, saison="add", periode=12):
    """Holt-Winters avec initialisation sur les deux premières saisons.

    On retire la tendance initiale avant d'estimer les indices saisonniers.
    Les deux premières saisons servent seulement à l'initialisation ; leurs
    erreurs ne participent pas au choix des paramètres sur l'apprentissage.
    """
    y = np.asarray(y, dtype=float)
    if saison not in ("add", "mul"):
        raise ValueError("La saisonnalité doit être 'add' ou 'mul'.")
    if len(y) < 2 * periode:
        raise ValueError("Deux saisons complètes sont nécessaires.")
    if saison == "mul" and np.any(y <= 0):
        raise ValueError("Le modèle multiplicatif exige des données positives.")
    debut = 2 * periode
    pente = (np.mean(y[periode:debut]) - np.mean(y[:periode])) / periode
    tendance = np.mean(y[:debut]) + pente * (np.arange(debut) - (debut - 1) / 2)
    if saison == "add":
        indices = (y[:debut] - tendance).reshape(2, periode).mean(axis=0)
        indices -= indices.mean()
    else:
        if np.any(tendance <= 0):
            raise ValueError("La tendance initiale multiplicative doit être positive.")
        indices = (y[:debut] / tendance).reshape(2, periode).mean(axis=0)
        indices /= indices.mean()
    niveau = tendance[-1]
    ajustes = np.full(len(y), np.nan)
    for i in range(debut, len(y)):
        j = i % periode
        base = niveau + pente
        ancien_indice = indices[j]
        ajustes[i] = base + ancien_indice if saison == "add" else base * ancien_indice
        corrige = y[i] - ancien_indice if saison == "add" else y[i] / ancien_indice
        nouveau_niveau = alpha * corrige + (1 - alpha) * base
        pente = beta * (nouveau_niveau - niveau) + (1 - beta) * pente
        residu = y[i] - nouveau_niveau if saison == "add" else y[i] / nouveau_niveau
        indices[j] = gamma * residu + (1 - gamma) * ancien_indice
        niveau = nouveau_niveau
    base = niveau + pente * np.arange(1, horizon + 1)
    saisons = indices[(len(y) + np.arange(horizon)) % periode]
    previsions = base + saisons if saison == "add" else base * saisons
    return ajustes, previsions


def choisir_hw(y, horizon, saison, alphas=ALPHAS):
    """Choix discret par SCE à un pas sur l'apprentissage exclusivement."""
    meilleur = None
    for alpha in alphas:
        for beta in (0.01, 0.1, 0.3):
            for gamma in (0.1, 0.3, 0.5):
                ajustes, previsions = holt_winters(
                    y, alpha, beta, gamma, horizon, saison
                )
                erreur = sce(y[2 * PERIODE :], ajustes[2 * PERIODE :])
                if meilleur is None or erreur < meilleur[0]:
                    meilleur = (erreur, (alpha, beta, gamma), ajustes, previsions)
    return meilleur


def decorer(ax, titre, coupure, xlabel="Temps"):
    ax.set(title=titre, xlabel=xlabel, ylabel="Valeur")
    ax.axvline(coupure, color="gray", linestyle=":", label="Début du test / prévision")
    ax.grid(alpha=0.25)
    ax.legend(fontsize=8)


# %% Préparation : simulation et séparation apprentissage / test
# Les fonctions ci-dessus servent aux calculs. Les réponses commencent ici.
t = np.arange(1, 101)
epsilon = np.random.default_rng(42).normal(0, 1, 100)
X1 = epsilon
X2 = 0.5 * t + 2 * epsilon
X3 = 0.5 * t + 3 * np.cos(t * np.pi / 6) + epsilon
# Les 70 premiers points servent à apprendre, les 30 derniers à tester.
train1, test1 = X1[:70], X1[70:]


# %% Question 1 : X1, lissage exponentiel simple avec alpha = 0.5
print("\nQUESTION 1 — X1 : lissage simple avec α = 0.5")
ajustes, previsions = lissage_simple(train1, 0.5, 30)
fig, ax = plt.subplots(figsize=(10, 4), constrained_layout=True)
ax.plot(t, X1, color="black", linewidth=1, label="X1 observée")
ax.plot(t[:70], ajustes, label="Prévisions à un pas sur l'apprentissage")
ax.plot(t[70:], previsions, label="Prévision du test, α = 0.5")
decorer(ax, "Question 1 — X1 : lissage simple, α = 0.5", 70.5)
print(f"La prévision est constante, égale à {previsions[0]:.3f}.")
print("X1 est un bruit blanc : on ne peut pas prévoir ses fluctuations ; "
      "sa prévision théorique est sa moyenne, zéro.")


# %% Question 2 : X1, comparer graphiquement cinq valeurs de alpha
print("\nQUESTION 2 — X1 : comparaison graphique des valeurs de α")
previsions_simples = []
fig, ax = plt.subplots(figsize=(10, 4), constrained_layout=True)
ax.plot(t, X1, color="black", linewidth=1, label="X1 observée")
for alpha in ALPHAS:
    _, prev = lissage_simple(train1, alpha, 30)
    previsions_simples.append(prev)
    ax.plot(t[70:], prev, label=f"α = {alpha}")
    print(f"α = {alpha} : prévision constante à {prev[0]:.3f}.")
decorer(ax, "Question 2 — X1 : comparaison des lissages simples", 70.5)
print("α = 0.1 lisse fortement ; 0.3 réagit davantage ; 0.5 est intermédiaire ; "
      "0.7 et 0.9 dépendent surtout des dernières observations.")
print("Les cinq droites sont proches sur ce graphique : le choix visuel est "
      "difficile. Un faible α est cohérent avec un bruit blanc, mais la droite "
      "pour α = 0.9 semble ici mieux centrée sur les observations de test.")


# %% Question 3 : X1, calculer les SCE et sélectionner le meilleur alpha
print("\nQUESTION 3 — X1 : somme des carrés des erreurs sur le test")
erreurs_simples = []
for alpha, prev in zip(ALPHAS, previsions_simples):
    erreur = sce(test1, prev)
    erreurs_simples.append(erreur)
    print(f"α = {alpha} : SCE = {erreur:.3f}")
meilleur = int(np.argmin(erreurs_simples))
print(f"Meilleur α : {ALPHAS[meilleur]}, SCE = {erreurs_simples[meilleur]:.3f}.")
print("Ce résultat dépend du tirage : il ne signifie pas qu'un grand α "
      "est toujours préférable pour un bruit blanc.")


# %% Question 4 : reprendre les questions 1 à 3 avec le lissage double
print("\nQUESTION 4 — X1 : lissage exponentiel double de Brown")
# 4.a : prévision avec alpha = 0.5.
ajustes, prev = lissage_double(train1, 0.5, 30)
fig, axes = plt.subplots(1, 2, figsize=(13, 4), constrained_layout=True)
axes[0].plot(t[:70], ajustes, label="Prévisions à un pas")
axes[0].plot(t[70:], prev, label="Prévision du test, α = 0.5")
print(f"Avec α = 0.5, la pente prévue est {prev[1] - prev[0]:.3f}.")

# 4.b et 4.c : comparaison graphique et calcul des SCE.
erreurs_doubles = []
for alpha in ALPHAS:
    _, prev = lissage_double(train1, alpha, 30)
    erreur = sce(test1, prev)
    erreurs_doubles.append(erreur)
    axes[1].plot(t[70:], prev, label=f"α = {alpha}")
    print(f"α = {alpha} : pente = {prev[1] - prev[0]:.3f}, SCE = {erreur:.3f}")
for ax, titre in zip(axes, ("α = 0.5", "comparaison des α")):
    ax.plot(t, X1, color="black", linewidth=1, label="X1 observée")
    decorer(ax, f"Question 4 — X1 : lissage double, {titre}", 70.5)
meilleur = int(np.argmin(erreurs_doubles))
print(f"Meilleur α : {ALPHAS[meilleur]}, SCE = {erreurs_doubles[meilleur]:.3f}.")
print("α = 0.1 et 0.3 donnent des prévisions presque horizontales ; 0.5 introduit "
      "une baisse ; 0.7 et 0.9 l'amplifient fortement. Le graphique favorise donc "
      "les faibles α. Le lissage double extrapole ici une tendance artificielle.")


# %% Question 5 : reprendre les questions précédentes pour X2 et X3
print("\nQUESTION 5 — Lissages simples et doubles de X2 et X3")
for nom, y in (("X2", X2), ("X3", X3)):
    train, test = y[:70], y[70:]
    print(f"\nQuestion 5 — {nom}")
    fig, axes = plt.subplots(2, 2, figsize=(13, 8), constrained_layout=True)
    for ligne, (methode, fonction) in enumerate((
        ("simple", lissage_simple), ("double", lissage_double)
    )):
        # Reprise de la question 1 : alpha = 0.5.
        ajustes, prev = fonction(train, 0.5, 30)
        axes[ligne, 0].plot(t[:70], ajustes, label="Prévisions à un pas")
        axes[ligne, 0].plot(t[70:], prev, label="Prévision du test, α = 0.5")
        # Reprise des questions 2 et 3 : graphique, commentaires et SCE.
        erreurs = []
        for alpha in ALPHAS:
            _, prev = fonction(train, alpha, 30)
            erreur = sce(test, prev)
            erreurs.append(erreur)
            axes[ligne, 1].plot(t[70:], prev, label=f"α = {alpha}")
            print(f"{nom}, lissage {methode}, α = {alpha} : "
                  f"début = {prev[0]:.2f}, fin = {prev[-1]:.2f}, SCE = {erreur:.3f}")
        meilleur = int(np.argmin(erreurs))
        print(f"Meilleur α pour le lissage {methode} : {ALPHAS[meilleur]}.")
        for ax, titre in zip(axes[ligne], ("α = 0.5", "comparaison des α")):
            ax.plot(t, y, color="black", linewidth=1, label=f"{nom} observée")
            decorer(ax, f"Question 5 — {nom} : lissage {methode}, {titre}", 70.5)
    if nom == "X2":
        print("X2 présente une tendance croissante. Le lissage simple reste constant : "
              "0.1 prend beaucoup de retard, 0.3 à 0.9 le réduisent sans prévoir la hausse. "
              "Avec Brown, 0.1 et 0.3 suivent bien la tendance ; 0.5 la sous-estime, "
              "0.7 l'aplatit et 0.9 prévoit même une baisse. Le graphique favorise 0.1 ou 0.3.")
    else:
        print("X3 a une tendance et une saisonnalité. En lissage simple, augmenter α "
              "réduit ici le retard mais aucune oscillation n'est prévue. En double, "
              "0.1 donne la meilleure pente ; 0.3 la surestime, et 0.5, 0.7, 0.9 "
              "extrapolent fortement la dernière hausse saisonnière.")

# Complément demandé dans la question 5 : Holt-Winters sur X3.
print("\nQuestion 5 — X3 : Holt-Winters additif et multiplicatif, période 12")
train, test = X3[:70], X3[70:]
# X3 contient des valeurs négatives. Pour le modèle multiplicatif uniquement,
# on décale l'apprentissage vers des valeurs positives, puis on retire ce décalage.
decalage = max(0.0, 1.0 - float(train.min()))
fig, axes = plt.subplots(1, 2, figsize=(13, 4), constrained_layout=True)
for ax, saison in zip(axes, ("add", "mul")):
    translation = decalage if saison == "mul" else 0.0
    erreurs = []
    for alpha in ALPHAS:
        # β et γ sont choisis sur l'apprentissage uniquement.
        _, params, _, prev = choisir_hw(train + translation, 30, saison, (alpha,))
        prev = prev - translation
        erreur = sce(test, prev)
        erreurs.append(erreur)
        ax.plot(t[70:], prev, label=f"α = {alpha}")
        print(f"HW {saison}, α,β,γ = {params} : SCE = {erreur:.3f}")
    meilleur = int(np.argmin(erreurs))
    print(f"Meilleur α pour HW {saison} : {ALPHAS[meilleur]}.")
    ax.plot(t, X3, color="black", linewidth=1, label="X3 observée")
    decorer(ax, f"Question 5 — X3 : Holt-Winters {saison}", 70.5)
print(f"Décalage appliqué au modèle multiplicatif : {decalage:.3f}.")
print("L'additif reproduit les oscillations d'amplitude constante : 0.3 donne ici "
      "la plus petite SCE ; 0.1 et 0.5 sont proches, 0.7 et 0.9 réagissent davantage "
      "au bruit. Le multiplicatif amplifie trop la saisonnalité : 0.1 limite cet "
      "effet, qui devient marqué pour 0.3 à 0.9. Le graphique favorise l'additif.")
print("Comme demandé, α est sélectionné sur le test : l'erreur retenue n'est "
      "donc pas une évaluation indépendante du modèle sélectionné.")


# %% Partie CO2 — Étape 1 : représenter les données et choisir un modèle
# Cette partie est un paragraphe non numéroté dans le sujet.
print("\nCO2 — ÉTAPE 1 : représentation et choix du modèle")
co2 = np.loadtxt("co2.txt")
dates = np.arange(np.datetime64("1959-01"), np.datetime64("1998-01"))
fig, ax = plt.subplots(figsize=(11, 4), constrained_layout=True)
ax.plot(dates, co2)
ax.set(title="CO2 — Étape 1 : observations de 1959 à 1997",
       xlabel="Année", ylabel="CO₂ (ppm)")
ax.grid(alpha=0.25)
print("On observe une tendance croissante et une saisonnalité annuelle "
      "d'amplitude assez stable. On choisit Holt-Winters additif, de période 12.")


# %% Partie CO2 — Étape 2 : apprendre sur 1959–1989, prévoir 1990–1997
print("\nCO2 — ÉTAPE 2 : validation sur 1990–1997")
coupure = 31 * 12
train, test = co2[:coupure], co2[coupure:]
_, params, _, prev = choisir_hw(train, len(test), "add")
erreur = sce(test, prev)
print(f"α,β,γ = {params} ; SCE = {erreur:.3f} ; "
      f"RMSE = {np.sqrt(erreur / len(test)):.3f} ppm.")
fig, ax = plt.subplots(figsize=(11, 4), constrained_layout=True)
ax.plot(dates, co2, color="black", linewidth=1, label="CO2 observé")
ax.plot(dates[coupure:], prev, label="Prévision de 1990 à 1997")
decorer(ax, "CO2 — Étape 2 : validation", np.datetime64("1990-01"), "Année")
ax.set_ylabel("CO₂ (ppm)")
print("La prévision suit bien la tendance et les cycles annuels. Elle surestime "
      "légèrement certaines observations, mais l'écart reste faible : "
      "la validation justifie de conserver ce modèle pour l'extrapolation.")


# %% Partie CO2 — Étape 3 : prévoir jusqu'en 2007
print("\nCO2 — ÉTAPE 3 : prévision jusqu'en 2007")
# 1997 est déjà observée : réestimation sur 1959–1997, puis prévision 1998–2007.
futures = np.arange(np.datetime64("1998-01"), np.datetime64("2008-01"))
_, params, _, prev = choisir_hw(co2, len(futures), "add")
fig, ax = plt.subplots(figsize=(11, 4), constrained_layout=True)
ax.plot(dates, co2, color="black", linewidth=1, label="CO2 observé")
ax.plot(futures, prev, label="Prévision de 1998 à 2007")
decorer(ax, "CO2 — Étape 3 : prévision jusqu'en 2007", np.datetime64("1998-01"), "Année")
ax.set_ylabel("CO₂ (ppm)")
print(f"Paramètres réestimés : α,β,γ = {params}.")
print("1997 étant connue, les 120 mois futurs vont de janvier 1998 à décembre 2007.")
for annee in range(1998, 2008):
    moyenne = prev[(annee - 1998) * 12:(annee - 1997) * 12].mean()
    print(f"Moyenne prévue pour {annee} : {moyenne:.2f} ppm")
print("Cette extrapolation prolonge la tendance et la saisonnalité ; "
      "elle ne peut pas anticiper une accélération de la croissance.")

plt.show()
