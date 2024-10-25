# Zoe LAVIEILLE - Simon DESDEVISES
# Tous les test de fonctions se trouvent en bas du fichier

# Question 1

# A
def piece_perdu(nb_tour):
    """
    Calcule le nombre de pièces perdues en fonction du nombre de tours
    Paramètre : nb_tour : Int
    Retourne : Int : Nombre de pièces perdues
    """
    piece = 8
    for i in range(1, nb_tour + 1):
        piece += 2*i - 10
    return piece

# Il y a 44 pièces perdues après 12 tours

# B

def calc_nb_tour(piece):
    """
    Calcule le nombre de tours en fonction du nombre de pièces perdues
    Paramètre : piece_perdu : Int
    Retourne : Int : Nombre de tours
    """
    index = 0
    while piece_perdu(index) < piece:
        index += 1
    return index

# Il faut 50 tours pour perdre 2024 pièces

# Question 2

# A

def init_matrice(n):
    """
    Crée une matrice de taille n*n
    Paramètre : n : Int
    Retourne : List[List[String]] : Matrice de taille n*n
    """
    matrice = []
    for i in range(n):
        matrice.append([""]*n)
    return matrice

def coordonne_centre(n):
    """
    Calcule la coordonné du centre de la matrice
    Paramètre : n : Int
    Retourne : (Int, Int) : coordonné du centre de la matrice
    """
    return n // 2, n // 2

def scenario(n):
    """
    Initialise le scenario avec les personnages
    Paramètre : n : Int
    Retourne : List[List[String]] : Matrice de taille n*n
    """
    if n == 0:
        return
    if n == 1:
        return [[]]
    matrice = init_matrice(n)
    x, y = coordonne_centre(n)
    matrice[x][y] = "V"
    matrice[0][0] = "T"
    matrice[0][n-1] = "S"
    matrice[n-1][0] = "M"
    matrice[n-1][n-1] = "P"
    return matrice


# B

"""
Quand n est pair le centre de la matrice n'est pas rééllement au centre, 
il est décalé d'une case vers la droite et le bas

Quand n est inférieur ou égal à 2, on ne peut pas placer les 5 personnages 
"""

# Question 3

def voleur(personnages):
    """
    Trouve le voleur parmis les personnages
    Paramètre : personnages : List[String]
    Retourne : String : Nom du voleur
    """
    result_voleur = ''
    for personnage in personnages:
        if len(personnage) > len(result_voleur):
            result_voleur = personnage
    return result_voleur



if __name__ == "__main__":
    assert piece_perdu(0) == 8
    assert piece_perdu(12) == 44

    assert calc_nb_tour(44) == 12
    assert calc_nb_tour(2024) == 50

    assert init_matrice(3) == [["", "", ""], ["", "", ""], ["", "", ""]]
    assert init_matrice(1) == [[""]]

    assert coordonne_centre(3) == (1, 1)
    assert coordonne_centre(4) == (2, 2)
    assert coordonne_centre(5) == (2, 2)

    def print_matrice(matrice):
        for i in matrice:
            print(i)

    assert scenario(3) == [['T', '', 'S'], ['', 'V', ''], ['M', '', 'P']]
    assert scenario(4) == [['T', '', '', 'S'], ['', '', '', ''], ['', '', 'V', ''], ['M', '', '', 'P']]

    assert voleur(["", "V", ""]) == "V"
    assert voleur(["Titi", "Zoe", "Simon"]) == "Simon"

    print("Tous les tests passent")