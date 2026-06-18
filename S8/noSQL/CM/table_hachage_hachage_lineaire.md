# Table de hachage et hachage linéaire

## 1. Table de hachage classique

Une **table de hachage** permet de ranger des données sous forme de paires :

```txt
(clé, valeur)
```

Exemple :

```txt
(21, Kaki)
(35, Kiwi)
(10, Noix)
```

L'objectif est de retrouver rapidement une valeur à partir de sa clé.

Pour cela, on utilise une **fonction de hachage**. Cette fonction prend une clé `k` et renvoie le numéro d'une **alvéole**, aussi appelée **bucket**.

Dans une table de hachage classique, si on a `M` buckets, on utilise souvent :

```txt
h(k) = k mod M
```

Le résultat indique dans quel bucket on place la clé.

---

## 2. Exemple de table de hachage classique

On a les clés suivantes :

```txt
21, 35, 10, 8, 34, 33, 14
```

On suppose que l'on a `M = 3` buckets :

```txt
bucket 0
bucket 1
bucket 2
```

La fonction de hachage est donc :

```txt
h(k) = k mod 3
```

Calculs :

| Clé | Calcul | Bucket |
|---:|---:|---:|
| 21 | 21 mod 3 = 0 | 0 |
| 35 | 35 mod 3 = 2 | 2 |
| 10 | 10 mod 3 = 1 | 1 |
| 8 | 8 mod 3 = 2 | 2 |
| 34 | 34 mod 3 = 1 | 1 |
| 33 | 33 mod 3 = 0 | 0 |
| 14 | 14 mod 3 = 2 | 2 |

État final :

```txt
bucket 0 : 21, 33
bucket 1 : 10, 34
bucket 2 : 35, 8, 14
```

---

## 3. Comment faire une table de hachage à la main

Méthode :

1. Identifier le nombre de buckets `M`.
2. Utiliser la fonction de hachage donnée, souvent `h(k) = k mod M`.
3. Calculer le reste de la division pour chaque clé.
4. Placer chaque clé dans le bucket correspondant.

Exemple rapide :

```txt
M = 4
h(k) = k mod 4
```

Pour la clé `10` :

```txt
10 mod 4 = 2
```

Donc la clé `10` va dans le bucket `2`.

---

# 4. Hachage linéaire

Le **hachage linéaire** est une version dynamique de la table de hachage.

Dans une table classique, le nombre de buckets est fixe. En hachage linéaire, on peut ajouter progressivement des buckets quand les alvéoles deviennent trop pleines.

On garde deux informations importantes :

```txt
n = niveau courant
p = pointeur vers le prochain bucket à diviser
```

On utilise deux fonctions de hachage :

```txt
h_n(k)     = k mod 2^n
h_{n+1}(k) = k mod 2^(n+1)
```

Pour trouver le bucket d'une clé `k`, on applique la règle :

```txt
i = h_n(k)

si i < p :
    i = h_{n+1}(k)
```

Cela signifie :

- on calcule d'abord avec la fonction du niveau courant ;
- si le bucket obtenu a déjà été divisé, on recalcule avec la fonction suivante.

---

## 5. Que se passe-t-il quand un bucket déborde ?

Dans le cours, chaque bucket a une taille maximale.

Si un bucket déborde :

1. on ajoute un nouveau bucket ;
2. on divise le bucket pointé par `p` ;
3. on redistribue les clés de ce bucket avec `h_{n+1}` ;
4. on avance `p` de 1 ;
5. si `p = 2^n`, alors on remet `p = 0` et on augmente `n` de 1.

Attention : le bucket qui déborde n'est pas forcément celui qui est divisé. On divise toujours le bucket pointé par `p`.

---

# 6. Exercice du cours

Énoncé :

> Quel est l'état final d'un hachage linéaire où on a une taille d'alvéole de 2 et un ensemble de clés `3, 6, 12, 32, 33, 54, 7, 21, 34, 11`, ajouté dans cet ordre ?

On part avec :

```txt
n = 1
p = 0
M = 2 buckets : 0 et 1
```

La taille maximale d'un bucket est :

```txt
2 clés
```

Au départ, on utilise :

```txt
h_1(k) = k mod 2
```

---

## Étape 1 — insertion de 3

```txt
3 mod 2 = 1
```

```txt
bucket 0 :
bucket 1 : 3
```

État :

```txt
n = 1, p = 0
```

---

## Étape 2 — insertion de 6

```txt
6 mod 2 = 0
```

```txt
bucket 0 : 6
bucket 1 : 3
```

État :

```txt
n = 1, p = 0
```

---

## Étape 3 — insertion de 12

```txt
12 mod 2 = 0
```

```txt
bucket 0 : 6, 12
bucket 1 : 3
```

Le bucket 0 est plein, mais il ne déborde pas encore.

État :

```txt
n = 1, p = 0
```

---

## Étape 4 — insertion de 32

```txt
32 mod 2 = 0
```

Le bucket 0 déborde :

```txt
bucket 0 : 6, 12, 32
bucket 1 : 3
```

On ajoute donc un bucket 2 et on divise le bucket pointé par `p = 0`.

On redistribue les clés du bucket 0 avec :

```txt
h_2(k) = k mod 4
```

Calculs :

| Clé | Calcul | Nouveau bucket |
|---:|---:|---:|
| 6 | 6 mod 4 = 2 | 2 |
| 12 | 12 mod 4 = 0 | 0 |
| 32 | 32 mod 4 = 0 | 0 |

Nouvel état :

```txt
bucket 0 : 12, 32
bucket 1 : 3
bucket 2 : 6
```

On avance `p` :

```txt
n = 1, p = 1
```

---

## Étape 5 — insertion de 33

On calcule d'abord :

```txt
33 mod 2 = 1
```

Comme `1 < p` est faux, on garde le bucket 1.

```txt
bucket 0 : 12, 32
bucket 1 : 3, 33
bucket 2 : 6
```

État :

```txt
n = 1, p = 1
```

---

## Étape 6 — insertion de 54

On calcule d'abord :

```txt
54 mod 2 = 0
```

Comme `0 < p`, on recalcule avec :

```txt
54 mod 4 = 2
```

Donc 54 va dans le bucket 2.

```txt
bucket 0 : 12, 32
bucket 1 : 3, 33
bucket 2 : 6, 54
```

État :

```txt
n = 1, p = 1
```

---

## Étape 7 — insertion de 7

On calcule :

```txt
7 mod 2 = 1
```

Le bucket 1 déborde :

```txt
bucket 1 : 3, 33, 7
```

On ajoute un bucket 3 et on divise le bucket pointé par `p = 1`.

On redistribue les clés du bucket 1 avec :

```txt
h_2(k) = k mod 4
```

Calculs :

| Clé | Calcul | Nouveau bucket |
|---:|---:|---:|
| 3 | 3 mod 4 = 3 | 3 |
| 33 | 33 mod 4 = 1 | 1 |
| 7 | 7 mod 4 = 3 | 3 |

Nouvel état :

```txt
bucket 0 : 12, 32
bucket 1 : 33
bucket 2 : 6, 54
bucket 3 : 3, 7
```

On avance `p` :

```txt
p = 2
```

Or :

```txt
2^n = 2^1 = 2
```

Donc `p` revient à 0 et `n` augmente :

```txt
n = 2, p = 0
```

---

## Étape 8 — insertion de 21

Maintenant on utilise :

```txt
h_2(k) = k mod 4
```

Calcul :

```txt
21 mod 4 = 1
```

```txt
bucket 0 : 12, 32
bucket 1 : 33, 21
bucket 2 : 6, 54
bucket 3 : 3, 7
```

État :

```txt
n = 2, p = 0
```

---

## Étape 9 — insertion de 34

```txt
34 mod 4 = 2
```

Le bucket 2 déborde :

```txt
bucket 2 : 6, 54, 34
```

On ajoute un bucket 4 et on divise le bucket pointé par `p = 0`.

Attention : le bucket qui déborde est le bucket 2, mais le bucket divisé est le bucket 0, car `p = 0`.

On redistribue donc les clés du bucket 0 avec :

```txt
h_3(k) = k mod 8
```

Calculs :

| Clé | Calcul | Nouveau bucket |
|---:|---:|---:|
| 12 | 12 mod 8 = 4 | 4 |
| 32 | 32 mod 8 = 0 | 0 |

Nouvel état :

```txt
bucket 0 : 32
bucket 1 : 33, 21
bucket 2 : 6, 54, 34
bucket 3 : 3, 7
bucket 4 : 12
```

On avance `p` :

```txt
n = 2, p = 1
```

---

## Étape 10 — insertion de 11

On calcule :

```txt
11 mod 4 = 3
```

Le bucket 3 déborde :

```txt
bucket 3 : 3, 7, 11
```

On ajoute un bucket 5 et on divise le bucket pointé par `p = 1`.

Attention : le bucket qui déborde est le bucket 3, mais le bucket divisé est le bucket 1, car `p = 1`.

On redistribue les clés du bucket 1 avec :

```txt
h_3(k) = k mod 8
```

Calculs :

| Clé | Calcul | Nouveau bucket |
|---:|---:|---:|
| 33 | 33 mod 8 = 1 | 1 |
| 21 | 21 mod 8 = 5 | 5 |

Nouvel état :

```txt
bucket 0 : 32
bucket 1 : 33
bucket 2 : 6, 54, 34
bucket 3 : 3, 7, 11
bucket 4 : 12
bucket 5 : 21
```

On avance `p` :

```txt
n = 2, p = 2
```

---

# 7. État final

L'état final est donc :

```txt
n = 2
p = 2
M = 6 buckets
```

Table finale :

```txt
bucket 0 : 32
bucket 1 : 33
bucket 2 : 6, 54, 34
bucket 3 : 3, 7, 11
bucket 4 : 12
bucket 5 : 21
```

Comme la taille d'alvéole est 2, les buckets 2 et 3 ont une case de débordement :

```txt
bucket 0 : [32]
bucket 1 : [33]
bucket 2 : [6, 54] -> débordement : [34]
bucket 3 : [3, 7]  -> débordement : [11]
bucket 4 : [12]
bucket 5 : [21]
```

---

# 8. Résumé de la méthode pour réussir l'exercice

À chaque insertion :

1. Calculer le bucket avec `h_n(k) = k mod 2^n`.
2. Si le résultat est inférieur à `p`, recalculer avec `h_{n+1}(k) = k mod 2^(n+1)`.
3. Insérer la clé dans le bucket trouvé.
4. Si le bucket dépasse la taille maximale :
   - ajouter un nouveau bucket ;
   - diviser le bucket `p` ;
   - redistribuer les clés de ce bucket avec `h_{n+1}` ;
   - avancer `p` ;
   - si `p = 2^n`, remettre `p = 0` et faire `n = n + 1`.

Le piège principal : **on ne divise pas forcément le bucket qui déborde, on divise le bucket pointé par `p`.**
