import numpy as np


def page_rank(
    matrix_norm: np.ndarray,
    beta: float = 0.85,
    epsilone: float = 1e-8,
    number_of_iterations: int = 1000,
    v: np.ndarray = None,
):
    n = matrix_norm.shape[0]
    if v is None:
        v = np.ones(n) / n
    beta_p = beta * matrix_norm.transpose()
    q = np.ones(n) / n
    count = 0
    for _ in range(number_of_iterations):
        q_prev = q.copy()
        q = beta_p @ q + (1 - beta) * v
        count += 1
        if np.linalg.norm(q - q_prev) < epsilone:
            break
    return (q, count)


if __name__ == "__main__":
    matrix_test = np.array(
        [[0, 1, 0, 0], [1, 0, 1 / 2, 0], [0, 0, 0, 1], [0, 0, 1 / 2, 0]], dtype=float
    )
    print("Standard PageRank:")
    print(page_rank(matrix_test.T, 0.85, 1e-8, 1000))

    print("\nPersonalized PageRank (favoring node 0):")
    v_custom = np.array([1, 0, 0, 0], dtype=float)
    print(page_rank(matrix_test.T, 0.85, 1e-8, 1000, v_custom))
