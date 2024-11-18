import numpy as np

# Question 1

# Dans Array object -> scalar
def question1():
    print("32 ---  min : ", np.finfo(np.float32).min, " max : ", np.finfo(np.float32).max)
    print("64 ---  min : ", np.finfo(np.float64).min, " max : ", np.finfo(np.float64).max)
    a = np.float32(3)
    print("type de a : ", np.dtype(a))
    print("Generation de inf par calcul : np.float32(1)/np.float32(0) = ", np.float32(1)/np.float32(0))
    print("Generation de nan par calcul : np.float32(0)/np.float32(0) = ", np.float32(0)/np.float32(0))


# Question 2

if __name__ == '__main__':
    question1()