import matplotlib.pyplot as plt
import numpy as np
from statsmodels.tsa.holtwinters import ExponentialSmoothing


def load_data(filename="co2.txt"):
    with open(filename, "r") as f:
        data = f.readlines()
        res = np.array([float(line.strip()) for line in data])
        f.close()
        return res


global_data = load_data()


def create_range_date(start, end):
    return np.arange(start, end, dtype="datetime64[M]")


def display_data(data=global_data):
    plt.plot(create_range_date("1959-01", "1998-01"), data, label="CO2")
    plt.xlabel("Années")
    plt.ylabel("CO2")
    plt.legend()


def holt_winters(X, alpha, horizon):
    modele = ExponentialSmoothing(
        X,
        trend="add",
        seasonal="add",
        seasonal_periods=12,
        initialization_method="estimated",
    )

    resultat = modele.fit(smoothing_level=alpha)

    return resultat.forecast(horizon)


def generate_train_test_split(data=global_data, split_year=1990):
    split_index = (split_year - 1959) * 12

    train = data[:split_index]
    test = data[split_index:]
    return train, test


def display_results(data=global_data):
    train, _test = generate_train_test_split(data)
    results = holt_winters(train, 0.5, 18 * 12)

    plt.plot(create_range_date("1959-01", "1998-01"), data, label="CO2")
    plt.plot(create_range_date("1990-01", "2008-01"), results, label="Holt-Winters")
    plt.xlabel("Années")
    plt.ylabel("CO2")
    plt.legend()


if __name__ == "__main__":
    # display_data()
    display_results()

    plt.show()
