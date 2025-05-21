import math

A= \
[
    [2, 1, 0, 4],
    [-2, -2, 3, -5],
    [6, 1, -2, 3],
    [2, -3, -12, -1],
    [0, 1, 6, 0],
    [4, -5, -3, -2]
]

b = \
[
    [2],
    [-9],
    [2],
    [2],
    [-1],
    [0.5]
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

def replace_column(M: list[list[float]], column: int, new_column: list[list[float]]) -> list[list[float]]:
    for i in range(len(M)):
        M[i][column] = new_column[i][0]
    return M

def remove_first_column(M: list[list[float]]) -> list[list[float]]:
    return [M[i][1:] for i in range(len(M))]

def remove_first_row(M: list[list[float]]) -> list[list[float]]:
    return M[1:]

def algorithme(A: list[list[float]], b: list[list[float]]):
    new_A = A[:]
    new_b = b[:]
    for round in range(len(A[0])):
        norme_A = norme_first_column(new_A)
        v = calcule_v(new_A, norme_A)
        vt = transpose(v)
        vt_v_2 = produit_vt_v(new_A, norme_A)
        
        for i in range(len(new_A[0])):
            column = get_column(new_A, i)
            vt_b = produit_vt_b(vt[0], column)
            new_column = b_moins(column, produit_const_v(vt_b / vt_v_2, v))
            new_A = replace_column(new_A[:], i, new_column)

        vt_b = produit_vt_b(vt[0], new_b)
        new_b = b_moins(new_b, produit_const_v(vt_b / vt_v_2, v))
        b = b[:round] + new_b[:]
        new_b = remove_first_row(new_b)
        new_A = remove_first_row(new_A)
        new_A = remove_first_column(new_A)

    print_matrix(b)
    return new_A




if __name__ == "__main__":
    assert get_column(A, 0) == [[2], [-2], [6], [2], [0], [4]]
    assert get_column(A, 1) == [[1], [-2], [1], [-3], [1], [-5]]
    assert get_column(A, 2) == [[0], [3], [-2], [-12], [6], [-3]]
    assert get_column(A, 3) == [[4], [-5], [3], [-1], [0], [-2]]

    assert replace_column(A[:], 0, get_column(A, 0)) == [[2, 1, 0, 4], [-2, -2, 3, -5], [6, 1, -2, 3], [2, -3, -12, -1], [0, 1, 6, 0], [4, -5, -3, -2]]
    # assert replace_column(A[:], 0, get_column(A, 1)) == [[1, 1, 0, 4], [-2, -2, 3, -5], [1, 1, -2, 3], [-3, -3, -12, -1], [1, 1, 6, 0], [-5, -5, -3, -2]]

    assert remove_first_column(A[:]) == [[1, 0, 4], [-2, 3, -5], [1, -2, 3], [-3, -12, -1], [1, 6, 0], [-5, -3, -2]]

    norme_A = norme_first_column(A)
    assert norme_A == 8

    v = calcule_v(A, norme_first_column(A))
    vt = transpose(v)
    assert v == [[10], [-2], [6], [2], [0], [4]]
    assert vt == [[10, -2, 6, 2, 0, 4]]
    
    vt_v_2 = produit_vt_v(A, norme_A)
    assert vt_v_2 == 80
    
    vt_b = produit_vt_b(vt[0], b)
    assert vt_b == 56
    
    # print_matrix(produit_const_v(vt_b / vt_v_2, v))
    # print_matrix(b_moins(b, produit_const_v(vt_b / vt_v_2, v)))
    algorithme(A, b)