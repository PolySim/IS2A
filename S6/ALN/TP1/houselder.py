import math
import numpy as np

def print_matrix(M: np.ndarray):
    for row in M:
        print('\t'.join(f"{x:.2f}" for x in row))

def norme_first_column(M: np.ndarray) -> np.float64:
    return math.sqrt(sum(x[0] ** 2 for x in M))

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
    return sum(vt[0, i] * b[i, 0] for i in range(len(b)))

def produit_v_vt(v: np.ndarray, vt: np.ndarray) -> np.ndarray:
    res = np.zeros((len(v), len(vt)), dtype=np.float64)
    for i in range(len(v)):
        for j in range(len(vt)):
            res[i, j] = v[i][0] * vt[j]
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

def algorithme(A: np.ndarray, b: np.ndarray, with_print: bool = False):
    # Clone matrice
    new_A = A.copy()
    new_b = b.copy()

    for round in range(A.shape[1]):
        # Calcule les données identique 
        norme_A = norme_first_column(new_A)
        v = calcule_v(new_A, norme_A)
        vt = transpose(v)
        vt_v_2 = produit_vt_v(new_A, norme_A)
        
        for i in range(new_A.shape[1]):
            # Pour chaque colonne de la sous matrice
            column = get_column(new_A, i)
            vt_b = produit_vt_b(vt, column)
            new_column = b_moins(column, produit_const_v(vt_b / vt_v_2, v))
            # Met à jour la matrice A et new_A
            new_A = replace_column(new_A, i, new_column)
            A = replace_column(A, i + round, new_column, start=round, with_0=i == 0)

        # Met à jour la matrice b et new_b
        vt_b = produit_vt_b(vt, new_b)
        new_b = b_moins(new_b, produit_const_v(vt_b / vt_v_2, v))
        b[round:] = new_b

        # Supprime la première ligne et la première colonne de la matrice new_A et new_b pour la prochaine itération
        new_b = remove_first_row(new_b)
        new_A = remove_first_row(new_A)
        new_A = remove_first_column(new_A)

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
    return res

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

    algorithme(A, b, with_print=True)