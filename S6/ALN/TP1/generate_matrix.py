import numpy as np

def print_matrix(M: np.ndarray):
    for row in M:
        print('\t'.join(f"{float(x):.2f}" for x in row))

def generate_matrix(n: int, m: int, min: float = -10, max: float = 10) -> np.ndarray:
    return np.random.uniform(min, max, (n, m))

def generate_vector(n: int, min: float = -10, max: float = 10) -> np.ndarray:
    return np.random.uniform(min, max, (n, 1))

if __name__ == "__main__":
    print("Matrix:\n")
    print_matrix(generate_matrix(3, 3))
    print("\n")
    print("Vector:\n")
    print_matrix(generate_vector(3))