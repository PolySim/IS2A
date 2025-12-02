from calendar import c

import numpy as np

beta = 0.85


def page_rank(
    matrix_norm: np.ndarray, epsilone: float = 1e-8, number_of_iterations: int = 100
):
    n = matrix_norm.shape[0]
    beta_p = beta * matrix_norm.transpose()
    q = np.ones(n) / n
    count = 0
    for _ in range(number_of_iterations):
        q_prev = q.copy()
        q = beta_p @ q + ((1 - beta) / n) * np.ones(n)
        count += 1
        if np.linalg.norm(q - q_prev) < epsilone:
            break
    return (q, count)


if __name__ == "__main__":
    matrix_test = np.array(
        [[0, 1, 0, 0], [1, 0, 1 / 2, 0], [0, 0, 0, 1], [0, 0, 1 / 2, 0]], dtype=float
    )
    print(page_rank(matrix_test, 2))
