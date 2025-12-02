import numpy as np


def create_map_key(data: list[list[str]]) -> tuple[dict[str, int], int]:
    map_key = {}
    current_index = 0
    for path in data:
        for page in path:
            if page not in map_key:
                map_key[page] = current_index
                current_index += 1
    return (map_key, current_index - 1)


def get_name_by_index(map_key: dict[str, int], index: int) -> str:
    for name, idx in map_key.items():
        if idx == index:
            return name
    return ""


def group_by_city(data: list[list[str]]) -> tuple[np.ndarray, dict[str, int]]:
    map_key, size = create_map_key(data)
    res = np.zeros((size + 1, size + 1), dtype=int)
    for row in data:
        for i in range(len(row) - 1):
            page = (row[i], map_key[row[i]])
            next_page = (row[i + 1], map_key[row[i + 1]])
            res[page[1]][next_page[1]] += 1
    return (res, map_key)


def normalize_matrix(matrix: np.ndarray) -> np.ndarray:
    row_sums = matrix.sum(axis=1)
    normalized_matrix = np.zeros_like(matrix, dtype=np.float64)
    for i in range(matrix.shape[0]):
        if row_sums[i] != 0:
            normalized_matrix[i] = matrix[i] / row_sums[i]
    return normalized_matrix


if __name__ == "__main__":
    from load_data import load_data

    data = load_data("filtered_hard.tsv")
    (matrix_initial, _) = group_by_city(data)
    matrix_norm = normalize_matrix(matrix_initial)
    print(matrix_norm)
    np.savetxt("matrix_initial.csv", matrix_norm, delimiter=";", fmt="%f")
