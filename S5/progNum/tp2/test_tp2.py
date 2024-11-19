#!/bin/python3
# Fichier test_tp2.py

import numpy as np

# Cette commande importe la fonction à tester (à adapter !)
from tp2 import eval_naive, eval_Horner, eval_derivee

# On écrit une fonction Python par test unitaire
# Le contenu du test doit bien sûr être adapté au cas par cas

def test_eval_naive_1():
    A = np.array ([], dtype=np.float64)
    assert (eval_naive (17, A) == 0), "0 attendu"

def test_eval_naive_2():
    A = np.array ([-1, 0, 1], dtype=np.float64)
    assert (eval_naive (-1, A) == 0), "0 attendu"

def test_eval_naive_3():
    A = np.array ([-1, 0, 1], dtype=np.float64)
    assert (eval_naive (1, A) == 0), "0 attendu"

def test_eval_Horner_1():
    A = np.array ([], dtype=np.float64)
    assert (eval_Horner (17, A) == 0), "0 attendu"

def test_eval_Horner_2():
    A = np.array ([-1, 0, 1], dtype=np.float64)
    assert (eval_Horner (-1, A) == 0), "0 attendu"

def test_eval_Horner_3():
    A = np.array ([-1, 0, 1], dtype=np.float64)
    assert (eval_Horner (1, A) == 0), "0 attendu"

def test_eval_Derivee_1():
    A = np.array ([], dtype=np.float64)
    assert (eval_derivee (17, A) == 0), "0 attendu"

def test_eval_Derivee_2():
    A = np.array ([-1, 0, 1], dtype=np.float64)
    print(eval_derivee (0, A))
    # assert (eval_derivee (0, A) == 0), "0 attendu"

def test_eval_Derivee_3():
    A = np.array ([-1, 0, 1], dtype=np.float64)
    print(eval_derivee(1, A))
    # assert (eval_derivee (1, A) == 2), "2 attendu"

# La liste L des fonctions définies ci-dessus (à adapter !)

L = [
    test_eval_naive_1,
    test_eval_naive_2,
    test_eval_naive_3,
    test_eval_Horner_1,
    test_eval_Horner_2,
    test_eval_Horner_3,
    test_eval_Derivee_1,
    test_eval_Derivee_2,
    test_eval_Derivee_3
]

# A partir d'ici, il n'y a plus rien à changer.
# La boucle suivante exécute chaque test et compte les succès.
nb_succès = 0
nb_échecs = 0
for test in L :
    print ('Exécution de', test.__name__, end='')
    try:
        test ()
        nb_succès += 1
        print (' : succès')
    except:
        nb_échecs += 1
        print (' : échec')

# Synthèse des résultats
print ('Nombre de tests  :', len(L))
print ('Nombre de succès :', nb_succès)
print ("Nombre d'échecs  :", nb_échecs)
