# Contents

1 Analyse Descriptive 1
2 Analyse factorielle discriminante sur R 2
3 Evaluation des règles de classements 2
4 Analyse discriminante probabiliste 2

## 1 Analyse Descriptive

1. Chargez les données du fichier winequality-white.csv. Ce jeu de données comporte 4898 observations des variables suivantes :

| Variable             | Description                                    |
| -------------------- | ---------------------------------------------- |
| fixed acidity        | Acidité fixe (g/dm3)                           |
| volatile acidity     | Acidité volatile (g/dm3)                       |
| citric acid          | Acide citrique (g/dm3)                         |
| residual sugar       | Sucre résiduel (g/dm3)                         |
| chlorides            | Chlorures (g/dm3)                              |
| free sulfur dioxide  | Dioxyde de soufre libre (mg/dm3)               |
| total sulfur dioxide | Dioxyde de soufre total (mg/dm3)               |
| density              | Densité (g/cm3)                                |
| pH                   | Niveau de pH                                   |
| sulphates            | Sulfates (g/dm3)                               |
| alcohol              | Teneur en alcool (% vol.)                      |
| quality              | Qualité du vin (score sensoriel entre 0 et 10) |

Ce jeu de données contient des mesures physico-chimiques de vins blancs portugais, collectées dans le cadre d'une étude visant à évaluer leur qualité sensorielle. Il comprend 11 variables d'entrée (comme l'acidité, la teneur en sucre, la densité, ou encore le taux d'alcool) et 1 variable de sortie : la note de qualité, attribuée par des dégustateurs sur une échelle de 0 à 10. L'objectif sera d'utiliser ces données pour prédire la qualité des vins (variable quality) à partir de leurs caractéristiques physico-chimiques. On considérera le score quality comme une variable qualitative.

2. Réalisez une rapide analyse descriptive des données.
3. Choisissez des graphiques appropriés pour représenter les éventuels liens entre la variable quality et les variables quantitatives.

## 2 Analyse factorielle discriminante sur R

1. La fonction lda du package MASS permet de réaliser l'AFD et l'ADL. Avec l'aide de la fonction (?lda), déterminer comment réaliser l'AFD et l'ADL avec cette fonction. Dans le cas du jeu de données iris, cela aura-t-il un impact ?
2. Réaliser l'AFD à l'aide de la fonction lda.
3. Utiliser la fonction predict et l'AFD faite à l'aide de la fonction lda afin de prédire pour chaque individu du jeu de données une classe d'appartenance.
4. Dans la sortie on remarque que la fonction retourne les coefficients linéaires discriminants LD1 et LD2. Ceux-ci peuvent être récupérés par le champ scaling de l'objet retourné par la fonction lda. En multipliant la matrice de données par la matrice des coefficients linéaires discriminants, obtenir une projection des individus sur ces axes discriminants. Donner un graphe du premier plan factoriel.

## 3 Evaluation des règles de classements

1. En utilisant les prédictions de votre choix (celles calculées par vos soins ou celle données par la fonction predict), calculer la matrice de confusion à l'aide de la fonction table sur la classe réelle et la classe prédite.
2. Calculer le taux de bon classement (TBC) et le taux de mauvais classement (TMC).
3. Expliquer pourquoi la méthode utilisée peut souffrir d'un biais d'optimisme.
4. Découper les données en un échantillon d'apprentissage (70% des données) et un échantillon test (30% des données). Utilisez les données d'apprentissage pour apprendre un modèle d'AFD sur les données de test.
5. Estimez les taux de bon et mauvais classement sur les données d'apprentissage et celles de test. Estimez quelles sont les limites de cette méthode.
6. Afin de prendre en compte ces limites, on propose d'estimer les taux de bon et mauvais classement par validation croisée $k$-fold. Vous pourrez utiliser une boucle afin de coder la validation croisée $k$-fold et déterminer les taux de bon et mauvais classement.

## 4 Analyse discriminante probabiliste

1. A l'aide de la fonction lda, réalisez une analyse discriminante linéaire. Evaluez ses taux de bons et de mauvais classments. Comparez avec l'AFD.
2. Estimez les taux de bon et de mauvais classements de cette règle en utilisant la validation croisée (on notera que l'option CV de cette fonction permet de faire de la validation croisée leave-one-out).
3. A l'aide de la fonction qda (dont l'utilisation est similaire à celle de la fonction lda), réalisez une analyse discriminante quadratique. Evaluez ses taux de bons et de mauvais classments. Comparez avec l'AFD et l'ADL.
4. Estimez les taux de bon et de mauvais classements de cette règle en utilisant la validation croisée (on notera que l'option CV de cette fonction permet de faire de la validation croisée leave-one-out).
