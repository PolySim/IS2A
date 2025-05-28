import math

A_Test= \
[
    [2, 1, 0, 4],
    [-2, -2, 3, -5],
    [6, 1, -2, 3],
    [2, -3, -12, -1],
    [0, 1, 6, 0],
    [4, -5, -3, -2]
]

b_Test = \
[
    [2],
    [-9],
    [2],
    [2],
    [-1],
    [0.5]
]

A= \
[
    [2, 1, 0, 4],
    [-4, -2, 3, -5],
    [4, 1, -2, 3],
    [0, -3, -12, -1],
]

b = \
[
    [2],
    [-9],
    [2],
    [2],
]

def print_matrix(M: list[list[float]]):
    for row in M:
        print('\t'.join(f"{x:.2f}" for x in row))

def norme_first_column(M: list[list[float]]):
    return math.sqrt(sum(x[0] ** 2 for x in M))

def calcule_v(M: list[list[float]], norme: float) -> list[list[float]]:
    v = []
    if M[0][0] > 0:
        v.append([M[0][0] + norme])
    else:
        v.append([M[0][0] - norme])
    for i in range(1, len(M)):
        v.append([M[i][0]])
    return v

def transpose(M: list[list[float]]) -> list[list[float]]:
    return [[M[j][i] for j in range(len(M))] for i in range(len(M[0]))]

def produit_vt_v(A: list[list[float]], norme: float) -> float:
    return norme * (norme + abs(A[0][0]))

def produit_vt_b(vt: list[float], b: list[list[float]]) -> float:
    return sum(vt[i] * b[i][0] for i in range(len(b)))

def produit_v_vt(v: list[list[float]], vt: list[float]) -> list[list[float]]:
    res = []
    for i in range(len(v)):
        res.append([v[i][0] * x for x in vt])
    return res

def produit_const_v(const: float, v: list[list[float]]) -> list[list[float]]:
    return [[const * x[0] for x in v]]

def b_moins(b: list[list[float]], v: list[list[float]]) -> list[list[float]]:
    return [[b[i][0] - v[0][i]] for i in range(len(b))]

def get_column(M: list[list[float]], column: int) -> list[list[float]]:
    return [[M[i][column]] for i in range(len(M))]

def replace_column(M: list[list[float]], column: int, new_column: list[list[float]], start: int = 0, with_0: bool = False) -> list[list[float]]:
    for i in range(start, len(M)):
        if (with_0 and i != start):
            M[i][column] = 0
        else:
            M[i][column] = new_column[i - start][0]
    return M

def remove_first_column(M: list[list[float]]) -> list[list[float]]:
    return [M[i][1:] for i in range(len(M))]

def remove_first_row(M: list[list[float]]) -> list[list[float]]:
    return M[1:]

def remonte(M: list[list[float]], b: list[list[float]]) -> list[list[float]]:
    res = []
    size_b = len(M[0])
    for i in range(size_b - 1, -1, -1):
        for j in range(size_b - 1, i - 1, -1):
            if (j == size_b - 1):
                if (i == size_b - 1):
                    res = [[b[j][0] / M[i][j]]] + res
                else:
                    res = [[M[i][j] * res[-1][0]]] + res
            else:
                if (j == i):
                    res[0][0] = (b[j][0] - res[0][0]) / M[i][j]
                else:
                    delta = size_b - j
                    res[0][0] += M[i][j] * res[-1 * delta][0]

    return res
        
def algorithme(A: list[list[float]], b: list[list[float]]):
    # Clone matrice
    new_A = A[:]
    new_b = b[:]

    for round in range(len(A[0])):

        # Calcule les données identique 
        norme_A = norme_first_column(new_A)
        v = calcule_v(new_A, norme_A)
        vt = transpose(v)
        vt_v_2 = produit_vt_v(new_A, norme_A)
        
        for i in range(len(new_A[0])):
            # Pour chaque colonne de la sous matrice
            column = get_column(new_A, i)
            vt_b = produit_vt_b(vt[0], column)
            new_column = b_moins(column, produit_const_v(vt_b / vt_v_2, v))
            # Met à jour la matrice A et new_A
            new_A = replace_column(new_A[:], i, new_column)
            A = replace_column(A[:], i + round, new_column, start=round, with_0=i == 0)

        # Met à jour la matrice b et new_b
        vt_b = produit_vt_b(vt[0], new_b)
        new_b = b_moins(new_b, produit_const_v(vt_b / vt_v_2, v))
        b = b[:round] + new_b[:]

        # Supprime la première ligne et la première colonne de la matrice new_A et new_b pour la prochaine itération
        new_b = remove_first_row(new_b)
        new_A = remove_first_row(new_A)
        new_A = remove_first_column(new_A)

    # Affiche les matrices A et b
    print("\nMatrice A\n")
    print_matrix(A)
    print("\nMatrice b\n")
    print_matrix(b)

    print("\nRésultat\n")
    res = remonte(A, b)
    print_matrix(res)
    return res




if __name__ == "__main__":
    assert get_column(A_Test, 0) == [[2], [-2], [6], [2], [0], [4]]
    assert get_column(A_Test, 1) == [[1], [-2], [1], [-3], [1], [-5]]
    assert get_column(A_Test, 2) == [[0], [3], [-2], [-12], [6], [-3]]
    assert get_column(A_Test, 3) == [[4], [-5], [3], [-1], [0], [-2]]

    assert replace_column(A_Test[:], 0, get_column(A_Test, 0)) == [[2, 1, 0, 4], [-2, -2, 3, -5], [6, 1, -2, 3], [2, -3, -12, -1], [0, 1, 6, 0], [4, -5, -3, -2]]

    norme_A = norme_first_column(A_Test)
    assert norme_A == 8

    v = calcule_v(A_Test, norme_first_column(A_Test))
    vt = transpose(v)
    assert v == [[10], [-2], [6], [2], [0], [4]]
    assert vt == [[10, -2, 6, 2, 0, 4]]
    
    vt_v_2 = produit_vt_v(A_Test, norme_A)
    assert vt_v_2 == 80
    
    vt_b = produit_vt_b(vt[0], b_Test)
    assert vt_b == 56
    
    # print_matrix(produit_const_v(vt_b / vt_v_2, v))
    # print_matrix(b_moins(b, produit_const_v(vt_b / vt_v_2, v)))
    # algorithme(A, b)

    # algorithme(A_Test, b_Test)