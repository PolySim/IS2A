import matplotlib.pyplot as plt
import numpy as np
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


def create_cumulative_data():
    initial_data = load_data()
    (matrix_initial, _) = group_by_city(initial_data)
    matrix_norm = normalize_matrix(matrix_initial)

    results = {}
    betas = [round(x * 0.2, 1) for x in range(5)]

    for beta in betas:
        (q, _) = page_rank(matrix_norm, beta, 1e-8)
        q_sorted = np.sort(q)[::-1]
        results[beta] = np.cumsum(q_sorted)

    n_values = len(next(iter(results.values())))

    with open("cumulative_data.csv", "w") as f:
        header = ["n"] + [f"beta_{b}" for b in betas]
        f.write(",".join(header) + "\n")

        for i in range(n_values):
            row = [str(i + 1)]
            for beta in betas:
                row.append(str(results[beta][i]))
            f.write(",".join(row) + "\n")


def display_cumulative_graph():
    data = {}
    ns = []

    with open("cumulative_data.csv", "r") as f:
        header = next(f).strip().split(",")
        betas = header[1:]

        for b in betas:
            data[b] = []

        for line in f:
            parts = line.strip().split(",")
            ns.append(int(parts[0]))
            for i, b in enumerate(betas):
                data[b].append(float(parts[i + 1]))

    plt.figure(figsize=(10, 6))
    for b in betas:
        label = b.replace("_", " ")
        plt.plot(ns, data[b], label=label)

    plt.title("Cumulative Sum of Top n PageRank Items")
    plt.xlabel("n")
    plt.ylabel("Cumulative Sum")
    plt.legend()
    plt.grid()
    plt.savefig("cumulative_plot.png")


if __name__ == "__main__":
    # print_number_of_iterations_by_beta()
    create_cumulative_data()
    display_cumulative_graph()
