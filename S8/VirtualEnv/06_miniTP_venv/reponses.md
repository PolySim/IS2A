# Réponses TP6 Venv

## 1. Créer et activer un environnement virtuel

```bash
python -m venv nom_du_venv
source nom_du_venv/bin/activate  # Linux/Mac
nom_du_venv\Scripts\activate      # Windows
```

## 2. Localisation du venv et site-packages

- Le venv est créé dans le dossier où vous lancez la commande
- Le dossier site-packages se trouve dans:
  - `nom_du_venv/lib/pythonX.X/site-packages/` (Linux/Mac)
  - `nom_du_venv\Lib\site-packages\` (Windows)

Pour le trouver, utilisez la commande suivante :

```bash
python -c "import site; print(site.getsitepackages())"
```

## 3. Scripts pour étudiants

**Étudiant A** (tableau pandas):

```python
import pandas as pd

classmates = {
    'Prénom': ['Alice', 'Bob', 'Charlie', 'Diana', 'Eve'],
    'Âge': [20, 21, 20, 22, 21],
    'Note': [15.5, 12.0, 14.0, 16.5, 13.5]
}
df = pd.DataFrame(classmates)
print(df)
```

**Étudiant B** (courbes matplotlib):

```python
import matplotlib.pyplot as plt

ages = [20, 21, 20, 22, 21]
notes = [15.5, 12.0, 14.0, 16.5, 13.5]

plt.figure(figsize=(8, 6))
plt.plot(ages, notes, 'o-', linewidth=2, markersize=8)
plt.xlabel('Âge')
plt.ylabel('Note moyenne')
plt.title('Âge vs Note moyenne des camarades')
plt.grid(True)
plt.show()
```

## 4. Protocole d'échange de code et dépendances

1. Créer un fichier `requirements.txt` avec `pip freeze > requirements.txt`
2. Partager le code source + requirements.txt
3. L'autre personne crée un venv et fait `pip install -r requirements.txt`
4. Alternative moderne: utiliser pyproject.toml avec poetry (gestion automatique des dépendances)
