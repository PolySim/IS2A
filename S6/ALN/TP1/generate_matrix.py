import random
import numpy as np
from houselder import print_matrix

def generate_matrix(n: int, m: int, min: float = -10, max: float = 10) -> np.ndarray:
    return np.random.uniform(min, max, (n, m))

def generate_vector(n: int, min: float = -10, max: float = 10) -> np.ndarray:
    return np.random.uniform(min, max, n)

if __name__ == "__main__":
    print("Matrix:\n")
    print_matrix(generate_matrix(3, 3))
    print("\n")
    print("Vector:\n")
    print_matrix([generate_vector(3)])