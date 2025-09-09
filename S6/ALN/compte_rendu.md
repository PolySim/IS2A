# Compte rendu résolution de système linéaire

## Introduction

Ce rapport présente l'implémentation et l'analyse comparative de deux méthodes de résolution de systèmes linéaires : la décomposition de Cholesky et la méthode de Householder. Ces deux approches ont été implémentées en Python en utilisant NumPy pour les calculs matriciels.

## Implémentation

### Méthode de Cholesky

La décomposition de Cholesky a été implémentée dans le fichier `cholesky.py`. Cette méthode décompose une matrice symétrique définie positive A en A = L·L^T, où L est une matrice triangulaire inférieure.

Les principales fonctions implémentées sont :

- `produit_matrix_transpose` : Calcule le produit d'une matrice par sa transposée
- `calcul_L` : Calcule la matrice L de la décomposition de Cholesky
- `descente` : Résout le système triangulaire inférieur L·z = b
- `algorithme` : Orchestre l'ensemble du processus de résolution

### Méthode de Householder

La méthode de Householder a été implémentée dans le fichier `houselder.py`. Cette approche utilise des réflexions de Householder pour transformer la matrice en une forme triangulaire supérieure.

Les fonctions principales incluent :

- `norme_first_column` : Calcule la norme de la première colonne
- `calcule_v` : Calcule le vecteur de Householder
- `remonte` : Résout le système triangulaire supérieur
- `algorithme` : Gère le processus complet de transformation et résolution

## Méthode des moindres carrés

### Moindres carrés avec Cholesky

La méthode de Cholesky peut être adaptée pour résoudre des problèmes de moindres carrés, même lorsque la matrice n'est pas carrée. Cette adaptation se fait en multipliant le système Ax = b par A^T, ce qui donne :

A^T·A·x = A^T·b

Cette transformation permet de :

- Transformer un système rectangulaire en un système carré
- Obtenir une matrice symétrique définie positive A^T·A
- Appliquer ensuite la décomposition de Cholesky classique

Dans notre implémentation, cela est réalisé par la fonction `produit_matrix_transpose` qui calcule A^T·A, rendant ainsi possible l'utilisation de Cholesky sur des matrices non carrées.

### Moindres carrés avec Householder

La méthode de Householder est particulièrement adaptée aux problèmes de moindres carrés car elle :

- Préserve la norme euclidienne des vecteurs
- Est numériquement stable
- Peut être appliquée directement sur des matrices rectangulaires

## Analyse des performances

### Temps d'exécution

Les deux méthodes ont été testées avec des matrices de taille croissante, de 4 à 2504 pour Cholesky et jusqu'à 1104 pour Householder. Les résultats montrent que :

1. **Méthode de Cholesky** :

   - Temps d'exécution pour n=4 : ~0.00015s
   - Temps d'exécution pour n=2504 : ~36.6s
   - Complexité observée : O(n³)
   - <img src="./TP1/Graphique de la complexité de l&apos;algorithme de Cholesky.png">

2. **Méthode de Householder** :
   - Temps d'exécution pour n=4 : ~0.00014s
   - Temps d'exécution pour n=1104 : ~77.45s
   - Complexité observée : O(n³)
   - <img src="./TP1/Graphique de la complexité de l&apos;algorithme de Householder.png">

### Précision des résultats

Les deux méthodes montrent une bonne précision numérique, avec des résidus très faibles :

1. **Méthode de Cholesky** :

   - Résidu pour n=4 : ~2.93e-14
   - Résidu pour n=2504 : ~7.01e-09

2. **Méthode de Householder** :
   - Résidu pour n=4 : ~4.44e-16
   - Résidu pour n=1104 : ~3.15e-12

### Impact de la précision des nombres flottants

L'utilisation de différents types de nombres flottants (float32 vs float64) a un impact significatif sur la précision des résultats :

1. **Float64 (double précision)** :

   - Précision d'environ 15-17 chiffres significatifs
   - Résidus observés de l'ordre de 10^-12 à 10^-14 pour les petites matrices
   - Dégradation progressive de la précision avec l'augmentation de la taille des matrices
   - Plus stable numériquement pour les grandes matrices

2. **Float32 (simple précision)** :
   - Précision d'environ 6-7 chiffres significatifs
   - Résidus généralement plus élevés (de l'ordre de 10^-6 à 10^-7)
   - Accumulation plus rapide des erreurs d'arrondi
   - Risque d'instabilité numérique plus important pour les grandes matrices

Cette différence de précision est particulièrement importante pour :

- Les matrices mal conditionnées
- Les systèmes de grande taille
- Les problèmes nécessitant une grande précision numérique

Le choix entre float32 et float64 doit donc être fait en fonction de :

- La précision requise pour l'application
- La taille des matrices à traiter
- Les contraintes de mémoire et de performance

## Conclusion

Les deux méthodes présentent des caractéristiques distinctes :

- La méthode de Cholesky est plus rapide pour les grandes matrices mais nécessite que la matrice soit symétrique définie positive. Grâce à la transformation par moindres carrés, elle peut également être utilisée pour des systèmes rectangulaires.
- La méthode de Householder est plus générale (fonctionne sur n'importe quelle matrice) et est particulièrement adaptée aux problèmes de moindres carrés, mais est plus coûteuse en temps de calcul.

Le choix entre ces deux méthodes dépend donc du contexte d'utilisation :

- Pour des matrices symétriques définies positives, Cholesky est préférable
- Pour des matrices générales ou des problèmes de moindres carrés, Householder est plus approprié malgré son coût de calcul plus élevé

## Annexes

```py
#cholesky.py

from houselder import print_matrix, transpose, remonte, residu, produit_matrix
import numpy as np
from generate_matrix import generate_matrix, generate_vector
import time

def produit_matrix_transpose(M_T: np.ndarray, M: np.ndarray) -> np.ndarray:
    A = np.zeros((M.shape[1], M.shape[1]), dtype=np.float64)
    m = M.shape[0]
    n = M.shape[1]

    for i in range(n):
        for j in range(i, n):
            # Peut être inversé
            somme = np.sum(M[:m, i] * M[:m, j])
            A[i, j] = somme
            if i != j:
                A[j, i] = somme
    return A

def calcul_L(A: np.ndarray) -> np.ndarray:
    n = A.shape[0]
    L = np.zeros((n, n))

    for row in range(n):
        for col in range(row + 1):
            # diagonale
            if row == col:
                somme = np.sum(L[row, :row] ** 2)
                L[row, col] = np.sqrt(A[row, col] - somme)
            else:
                somme = np.sum(L[row, :col] * L[col, :col])
                L[row, col] = (A[row, col] - somme) / L[col, col]
    return L

def descente(L: np.ndarray, b: np.ndarray) -> np.ndarray:
    n = b.shape[0]
    z = np.zeros((n, 1), dtype=np.float64)

    for i in range(n):
        somme = np.sum(L[i, :i] * z[:i, 0])
        z[i] = (b[i] - somme) / L[i, i]

    return z

def algorithme(C: np.ndarray, b: np.ndarray, with_print: bool = False):
    A = produit_matrix_transpose(transpose(C), C)
    b = produit_matrix(transpose(C), b)
    L = calcul_L(A)
    L_T = transpose(L)
    z = descente(L, b)
    x = remonte(L_T, z)
    residu_v = residu(A, b, x)
    norme_residu = np.sqrt(np.sum(residu_v ** 2))
    if with_print:
        print("\nMatrice A\n")
        print_matrix(A)
        print("\nMatrice L\n")
        print_matrix(L)
        print("\nMatrice L_T\n")
        print_matrix(L_T)
        print("\nMatrice z\n")
        print_matrix(z)
        print("\nMatrice x\n")
        print_matrix(x)
        print("\nRésidu\n")
        print_matrix(residu_v)
        print("Norme résidu : ", norme_residu)
    return norme_residu

if __name__ == "__main__":
    C = np.array([
        [1.0, 0.0, 1.0],
        [0.5, -1.0, 0.0],
        [0.0, 3.0, -5.0],
        [1.0, 0.0, -4.0]
    ], dtype=np.float64)

    A = np.array([
        [2.25, -0.5, -3.0],
        [-0.5, 10.0, -15.0],
        [-3.0, -15.0, 42.0]
    ], dtype=np.float64)

    A_non_carre = np.array([
        [2.0, 1.0, 0.0, 4.0],
        [-2.0, -2.0, 3.0, -5.0],
        [6.0, 1.0, -2.0, 3.0],
        [2.0, -3.0, -12.0, -1.0],
        [0.0, 1.0, 6.0, 0.0],
        [4.0, -5.0, -3.0, -2.0]
    ], dtype=np.float64)

    b = np.array([
        [1.0],
        [-1.0],
        [4.0]
    ], dtype=np.float64)

    b_non_carre = np.array([
        [2.0],
        [-9.0],
        [2.0],
        [2.0],
        [-1.0],
        [0.5]
    ], dtype=np.float64)

    n = 4
    with open("cholesky_carre_64.txt", "a") as f:
        while n <= 10_000:
            print(f"n = {n}")
            A = generate_matrix(n, n)
            b = generate_vector(n)
            start_time = time.time()
            a = algorithme(A, b)
            end_time = time.time()
            f.write(f"{n} {end_time - start_time} {a}\n")
            n += 100
```

```py
#houselder.py

import math
import numpy as np
from generate_matrix import generate_matrix, generate_vector
import time

def print_matrix(M: np.ndarray):
    for row in M:
        print('\t'.join(f"{float(x):.2f}" for x in row))

def norme_first_column(M: np.ndarray) -> np.float64:
    return np.sqrt(np.sum(M[:, 0] ** 2))

def calcule_v(M: np.ndarray, norme: float) -> np.ndarray:
    # Récupère la première colonne de la matrice M
    v = M[:, 0].copy().reshape(-1, 1)
    if M[0, 0] > 0:
        v[0] = M[0, 0] + norme
    else:
        v[0] = M[0, 0] - norme
    return v

def transpose(M: np.ndarray) -> np.ndarray:
    return M.T

def produit_vt_v(A: np.ndarray, norme: np.float64) -> np.float64:
    return norme * (norme + abs(A[0, 0]))

def produit_vt_b(vt: np.ndarray, b: np.ndarray) -> np.float64:
    return np.sum(vt[0, :] * b[:, 0])

def produit_v_vt(v: np.ndarray, vt: np.ndarray) -> np.ndarray:
    res = np.zeros((v.shape[0], vt.shape[0]), dtype=np.float64)
    for i in range(v.shape[0]):
        for j in range(vt.shape[0]):
            res[i, j] = v[i, 0] * vt[j, 0]
    return res

def produit_const_v(const: float, v: np.ndarray) -> np.ndarray:
    return const * v

def b_moins(b: np.ndarray, v: np.ndarray) -> np.ndarray:
    return b - v

def get_column(M: np.ndarray, column: int) -> np.ndarray:
    return M[:, column].reshape(-1, 1)

def replace_column(M: np.ndarray, column: int, new_column: np.ndarray, start: int = 0, with_0: bool = False) -> np.ndarray:
    for i in range(start, M.shape[0]):
        if (with_0 and i != start):
            M[i, column] = 0
        else:
            M[i, column] = new_column[i - start][0]
    return M

def remove_first_column(M: np.ndarray) -> np.ndarray:
    return M[:, 1:]

def remove_first_row(M: np.ndarray) -> np.ndarray:
    return M[1:]

def remonte(M: np.ndarray, b: np.ndarray) -> np.ndarray:
    res = np.zeros((M.shape[1], 1), dtype=np.float64)
    size_b = M.shape[1]
    for i in range(size_b - 1, -1, -1):
        for j in range(size_b - 1, i - 1, -1):
            if (j == size_b - 1):
                if (i == size_b - 1):
                    res[i, 0] = b[j, 0] / M[i, j]
                else:
                    res[i, 0] = M[i, j] * res[-1, 0]
            else:
                if (j == i):
                    res[i, 0] = (b[j, 0] - res[j, 0]) / M[i, j]
                else:
                    delta = size_b - j
                    res[i, 0] += M[i, j] * res[-1 * delta, 0]
    return res

def produit_matrix(A: np.ndarray, B: np.ndarray) -> np.ndarray:
    return A @ B

def soustraction_matrix(A: np.ndarray, B: np.ndarray) -> np.ndarray:
    return A - B

def residu(A: np.ndarray, b: np.ndarray, x: np.ndarray) -> np.ndarray:
    return produit_matrix(A, x) - b

def algorithme(A: np.ndarray, b: np.ndarray, with_print: bool = False) -> np.float64:
    for round in range(A.shape[1]):
        # Calcule les données identique
        norme_A = norme_first_column(A[round:, round:])
        v = calcule_v(A[round:, round:], norme_A)
        vt = transpose(v)
        vt_v_2 = produit_vt_v(A[round:, round:], norme_A)

        A = replace_column(A, round, v, start=round, with_0=True)
        for i in range(1, A[round:, round:].shape[1]):
            # Pour chaque colonne de la sous matrice
            column = get_column(A[round:, round:], i)
            vt_b = produit_vt_b(vt, column)
            new_column = b_moins(column, produit_const_v(vt_b / vt_v_2, v))
            # Met à jour la matrice A
            A = replace_column(A, i + round, new_column, start=round)

        # Met à jour la matrice b
        vt_b = produit_vt_b(vt, b[round:])
        b[round:] = b_moins(b[round:], produit_const_v(vt_b / vt_v_2, v))

    res = remonte(A, b)
    # Affiche les matrices A et b
    if with_print:
        print("\nMatrice A\n")
        print_matrix(A)
        print("\nMatrice b\n")
        print_matrix(b)
        print("\nRésultat\n")
        print_matrix(res)
        print("\nRésidu\n")
        print_matrix(residu(A, b, res))

    residu_v = residu(A, b, res)
    return np.sqrt(np.sum(residu_v ** 2))

if __name__ == "__main__":
    A_non_carre_test = np.array([
        [2, 1, 0, 4],
        [-2, -2, 3, -5],
        [6, 1, -2, 3],
        [2, -3, -12, -1],
        [0, 1, 6, 0],
        [4, -5, -3, -2]
    ], dtype=np.float64)

    b_non_carre_test = np.array([
        [2],
        [-9],
        [2],
        [2],
        [-1],
        [0.5]
    ], dtype=np.float64)

    A = np.array([
        [2, 1, 0, 4],
        [-4, -2, 3, -5],
        [4, 1, -2, 3],
        [0, -3, -12, -1],
    ], dtype=np.float64)

    b = np.array([
        [2],
        [-9],
        [2],
        [2],
    ], dtype=np.float64)

    assert np.array_equal(get_column(A_non_carre_test, 0), np.array([[2], [-2], [6], [2], [0], [4]]))
    assert np.array_equal(get_column(A_non_carre_test, 1), np.array([[1], [-2], [1], [-3], [1], [-5]]))
    assert np.array_equal(get_column(A_non_carre_test, 2), np.array([[0], [3], [-2], [-12], [6], [-3]]))
    assert np.array_equal(get_column(A_non_carre_test, 3), np.array([[4], [-5], [3], [-1], [0], [-2]]))

    assert np.array_equal(replace_column(A_non_carre_test.copy(), 0, get_column(A_non_carre_test, 0)), A_non_carre_test)

    norme_A = norme_first_column(A_non_carre_test)
    assert norme_A == 8

    v = calcule_v(A_non_carre_test, norme_first_column(A_non_carre_test))
    vt = transpose(v)
    assert np.array_equal(v, np.array([[10], [-2], [6], [2], [0], [4]]))
    assert np.array_equal(vt, np.array([[10, -2, 6, 2, 0, 4]]))

    vt_v_2 = produit_vt_v(A_non_carre_test, norme_A)
    assert vt_v_2 == 80

    vt_b = produit_vt_b(vt, b_non_carre_test)
    assert vt_b == 56

    # algorithme(A, b, with_print=True)
    n = 4
    with open("houselder_carre_64.txt", "a") as f:
        while n <= 100_000:
            print(f"n = {n}")
            A = generate_matrix(n, n)
            b = generate_vector(n)
            start_time = time.time()
            a = algorithme(A, b)
            end_time = time.time()
            f.write(f"{n} {end_time - start_time} {a}\n")
            n += 100
```

```py
#generate_matrix.py

import numpy as np

def print_matrix(M: np.ndarray):
    for row in M:
        print('\t'.join(f"{float(x):.2f}" for x in row))

def generate_matrix(n: int, m: int, min: float = -10, max: float = 10) -> np.ndarray:
    return np.random.uniform(min, max, (n, m))

def generate_vector(n: int, min: float = -10, max: float = 10) -> np.ndarray:
    return np.random.uniform(min, max, (n, 1))

if __name__ == "__main__":
    print("Matrix:\n")
    print_matrix(generate_matrix(3, 3))
    print("\n")
    print("Vector:\n")
    print_matrix(generate_vector(3))
```
