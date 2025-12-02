import numpy as np
from format_data import get_name_by_index, group_by_city, normalize_matrix
from load_data import load_data
from pageRank import page_rank

beta = 0.85


def main():
    initial_data = load_data()
    (matrix_initial, map_key) = group_by_city(initial_data)
    matrix_norm = normalize_matrix(matrix_initial)
    (res, count) = page_rank(matrix_norm, 1e-14, 1000)
    print("15 first cities with highest PageRank:")
    indices_sorted = np.argsort(res)[::-1]
    for i in range(15):
        print(
            f"page: {get_name_by_index(map_key, indices_sorted[i])}, PageRank: {res[indices_sorted[i]]}"
        )
    print(count)


if __name__ == "__main__":
    main()
