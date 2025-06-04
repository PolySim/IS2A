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

