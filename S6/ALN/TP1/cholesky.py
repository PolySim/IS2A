from houselder import print_matrix, transpose, remonte, residu
import numpy as np

C = np.array([
    [1.0, 0.0, 1.0],
    [0.5, -1.0, 0.0],
    [0.0, 3.0, -5.0],
    [1.0, 0.0, -4.0]
], dtype=np.float64)

b = np.array([
    [1.0],
    [-1.0],
    [4.0]
], dtype=np.float64)


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
    L = calcul_L(A)
    L_T = transpose(L)
    z = descente(L, b)
    x = remonte(L_T, z)
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
        print_matrix(residu(A, b, x))
    return x

if __name__ == "__main__":
    algorithme(C, b, with_print=True)