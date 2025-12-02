import matplotlib.pyplot as plt
from format_data import group_by_city, normalize_matrix
from load_data import load_data
from pageRank import page_rank


def write_result_in_csv(fileName: str, values: list[str]):
    with open(fileName, "w") as f:
        f.write("beta,number_of_iterations\n")
        for value in values:
            f.write(value)


def number_of_iteration_by_beta():
    step = 0.05
    res = []
    beta = 0
    while beta <= 1:
        initial_data = load_data()
        (matrix_initial, map_key) = group_by_city(initial_data)
        matrix_norm = normalize_matrix(matrix_initial)
        (_, count) = page_rank(matrix_norm, beta, 1e-8)
        res.append(f"{beta},{count}\n")
        beta += step
    write_result_in_csv("number_of_iterations_by_beta.csv", res)


def print_number_of_iterations_by_beta():
    res = []
    with open("number_of_iterations_by_beta.csv", "r") as f:
        next(f)
        for line in f:
            res.append(line.strip().split(","))
    plt.figure(figsize=(10, 6))
    plt.plot(
        [float(item[0]) for item in res],
        [int(item[1]) for item in res],
        marker="o",
        linestyle="-",
    )
    plt.title("Number of Iterations by Beta")
    plt.xlabel("Beta")
    plt.ylabel("Number of Iterations")
    plt.grid()
    plt.savefig("number_of_iterations_by_beta.png")


if __name__ == "__main__":
    print_number_of_iterations_by_beta()
