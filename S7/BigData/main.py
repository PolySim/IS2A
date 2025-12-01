import numpy as np
from format_data import group_by_city, normalize_matrix
from load_data import load_data
from pageRank import page_rank

beta = 0.85


def main():
    initial_data = load_data()
    matrix_initial = group_by_city(initial_data)
    matrix_norm = normalize_matrix(matrix_initial)
    res = page_rank(matrix_norm)


if __name__ == "__main__":
    main()
