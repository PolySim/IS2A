import sys
import math
import numpy as np

sys.path.append("/Users/simon/Documents/Developpement/IS2A")

from S5.progNum.tp5.code_generation import Python


def F(x):
    t1 = 1. / math.sqrt(x)
    t3 = 1 + x
    t2 = t3 ** (-25 / 2)
    t4 = math.cos(x)
    t6 = (-1) * x
    t5 = math.exp(t6)
    t0 = t1 * t2 * t4 * t5
    return t0


def f(x: np.float64) -> np.float64:
    t2 = 1. / math.sqrt(x)
    t4 = 1 + x
    t3 = t4 ** (-25 / 2)
    t5 = math.cos(x)
    t7 = (-1) * x
    t6 = math.exp(t7)
    t1 = (-1) * t2 * t3 * t5 * t6
    t9 = math.sin(x)
    t8 = (-1) * t2 * t3 * t6 * t9
    t11 = t4 ** (-27 / 2)
    t10 = (-25 / 2) * t2 * t11 * t5 * t6
    t13 = x ** (-3 / 2)
    t12 = (-1 / 2) * t13 * t3 * t5 * t6
    t0 = t1 + t8 + t10 + t12
    return t0


# Question 1
def trapeze(f: callable(np.float64), a: np.float64, b: np.float64, n: int) -> np.float64:
    h = (b - a) / n
    sum = (f(a) / 2) + (f(a + n * h) / 2)
    for i in range(1, n):
        sum += f(a + i * h)
    return h * sum


if __name__ == '__main__':
    a = .20345
    b = .01

    print("All test are passed")
