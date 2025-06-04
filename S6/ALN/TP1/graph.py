import matplotlib.pyplot as plt

def lire_donnees(fichier: str):
    x, y1 = [], []
    with open(fichier, 'r') as f:
        for ligne in f:
            if ligne.strip():
                parts = ligne.strip().split()
                if len(parts) != 3:
                    continue  # ignore les lignes incorrectes
                xi, yi1, _ = map(float, parts)
                x.append(xi)
                y1.append(yi1)
    return x, y1

def tracer(x, y1, title: str):
    plt.figure(figsize=(10, 6))
    plt.plot(x, y1, label='Temps d\'exécution', marker='o')
    plt.xlabel('Taille de la matrice')
    plt.ylabel('Temps (s)')
    plt.title(title)
    plt.legend()
    plt.grid(True)
    plt.tight_layout()
    plt.savefig(f"{title}.png")

def main():
    x, y1 = lire_donnees("houselder_carre_64.txt")
    tracer(x, y1, "Graphique de la complexité de l'algorithme de Householder")
    x, y1 = lire_donnees("cholesky_carre_64.txt")
    tracer(x, y1, "Graphique de la complexité de l'algorithme de Cholesky")

if __name__ == "__main__":
    main()
