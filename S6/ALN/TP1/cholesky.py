from houselder import print_matrix, transpose, remonte
import math

C = \
[
    [1.0, 0.0, 1.0],
    [0.5, -1.0, 0.0],
    [0.0, 3.0, -5.0],
    [1.0, 0.0, -4.0]
]

b = \
[
    [1.0],
    [-1.0],
    [4.0]
]

def init_matrix_carree(n: int) -> list[list[float]]:
    return [[0.0 for _ in range(n)] for _ in range(n)]

def produit_matrix_transpose(M_T: list[list[float]], M: list[list[float]]) -> list[list[float]]:
    m = len(M)
    n = len(M_T)
    A = init_matrix_carree(n)

    for i in range(n):
        # triangle supérieur
        for j in range(i, n):
            somme = 0.0
            # produit scalaire des colonnes i et j
            for k in range(m):
                somme += C[k][i] * C[k][j]
            A[i][j] = somme
            if i != j:
                # symétrie
                A[j][i] = somme

    return A

def calcul_L(A: list[list[float]]) -> list[list[float]]:
    n = len(A)
    L = init_matrix_carree(n)
    for row in range(n):
        for col in range(row + 1):
            # diagonale
            if row == col:
                somme = 0.0
                for diag in range(row):
                    somme += L[row][diag] ** 2
                L[row][col] = math.sqrt(A[row][col] - somme)
            else:
                somme = 0.0
                for diag in range(col):
                    somme += L[row][diag] * L[col][diag]
                L[row][col] = (A[row][col] - somme) / L[col][col]
    return L

def descente(L: list[list[float]], b: list[list[float]]) -> list[list[float]]:
    n = len(b)
    z = [[0.0] for _ in range(n)]

    for i in range(n):
        somme = 0.0
        for j in range(i):
            somme += L[i][j] * z[j][0]
        z[i][0] = (b[i][0] - somme) / L[i][i]

    return z


def algorithme(C: list[list[float]], b: list[list[float]]):
    A = produit_matrix_transpose(transpose(C), C)
    L = calcul_L(A)
    L_T = transpose(L)
    z = descente(L, b)
    x = remonte(L_T, z)
    print_matrix(x)

if __name__ == "__main__":
    algorithme(C, b)