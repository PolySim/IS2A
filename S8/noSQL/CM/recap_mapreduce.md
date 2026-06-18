# Récapitulatif — MapReduce

## 1. Définition

**MapReduce** est un modèle de traitement distribué qui permet de faire des calculs sur de très grands volumes de données.

L'idée principale est de découper les données en plusieurs blocs, de traiter chaque bloc séparément, puis de fusionner les résultats.

Dans le cours, MapReduce est présenté comme un patron d'architecture permettant d'effectuer des calculs parallèles et distribués. Plutôt que d'envoyer toutes les données vers un serveur central, on déplace le programme vers les données.

## 2. Principe général

MapReduce fonctionne en trois grandes étapes :

1. **Découper les données en blocs**
2. **Appliquer une fonction `Map` à chaque bloc**
3. **Appliquer une fonction `Reduce` pour fusionner les résultats**

## 3. La fonction Map

La fonction `Map` lit une donnée et produit une ou plusieurs paires :

```text
(clé, valeur)
```

Pour un comptage d'occurrences, la valeur est souvent `1`.

Exemple :

```text
Map("XBB")
→ (X, 1), (B, 1), (B, 1)
```

## 4. La phase de regroupement

Après l'étape `Map`, on regroupe toutes les paires qui ont la même clé.

Exemple :

```text
(B, 1), (B, 1), (B, 1)
→ B : [1, 1, 1]
```

Cette étape est souvent appelée **shuffle/sort**.

## 5. La fonction Reduce

La fonction `Reduce` reçoit une clé et la liste des valeurs associées, puis construit le résultat final par agrégation.

Exemple :

```text
Reduce(B, [1, 1, 1])
→ (B, 3)
```

## 6. Méthode pour faire un exercice à la main

Pour résoudre un exercice MapReduce à la main :

1. Identifier les blocs de données.
2. Appliquer `Map` sur chaque bloc.
3. Écrire toutes les paires produites.
4. Regrouper les paires par clé.
5. Appliquer `Reduce` pour obtenir le résultat final.

---

# Exercice — Comptage des lettres

## Énoncé

On a le texte suivant :

```text
{XBB, CBA, XAC}
```

On veut connaître le nombre d'occurrences de chaque lettre dans le texte.

Résultat attendu :

```text
{A : 2, B : 3, C : 2, X : 2}
```

## Étape 1 — Découpage en blocs

On découpe le texte en trois blocs :

```text
Bloc 1 : XBB
Bloc 2 : CBA
Bloc 3 : XAC
```

## Étape 2 — Fonction Map

La fonction `Map` produit une paire `(lettre, 1)` pour chaque lettre rencontrée.

Pseudo-code :

```text
Map(mot):
    pour chaque lettre dans mot:
        émettre (lettre, 1)
```

Application sur chaque bloc :

```text
Map("XBB") = (X, 1), (B, 1), (B, 1)
Map("CBA") = (C, 1), (B, 1), (A, 1)
Map("XAC") = (X, 1), (A, 1), (C, 1)
```

Toutes les paires obtenues sont donc :

```text
(X, 1), (B, 1), (B, 1),
(C, 1), (B, 1), (A, 1),
(X, 1), (A, 1), (C, 1)
```

## Étape 3 — Regroupement par clé

On regroupe les valeurs associées à chaque lettre :

```text
A → [1, 1]
B → [1, 1, 1]
C → [1, 1]
X → [1, 1]
```

## Étape 4 — Fonction Reduce

La fonction `Reduce` additionne les valeurs de chaque clé.

Pseudo-code :

```text
Reduce(lettre, liste_valeurs):
    somme = 0
    pour chaque valeur dans liste_valeurs:
        somme = somme + valeur
    émettre (lettre, somme)
```

Application :

```text
Reduce(A, [1, 1]) = (A, 2)
Reduce(B, [1, 1, 1]) = (B, 3)
Reduce(C, [1, 1]) = (C, 2)
Reduce(X, [1, 1]) = (X, 2)
```

## Résultat final

```text
{A : 2, B : 3, C : 2, X : 2}
```

## Résumé rapide

```text
Map : chaque lettre devient (lettre, 1)
Regroupement : on rassemble les mêmes lettres
Reduce : on additionne les valeurs pour chaque lettre
```

