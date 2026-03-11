# Réponses TP7 Pip Freeze

## 1. Écrire un fichier requirements.txt

```bash
pip freeze > requirements.txt
```

Contenu typique:
```
certifi==2026.2.25
numpy==2.4.2
pybind11==3.0.2
wheel==0.46.3
```

## 2-3. Partager et reproduire l'environnement

**Étapes pour le voisin:**

```bash
# Créer un nouvel environnement virtuel
python -m venv env_voisin
source env_voisin/bin/activate

# Installer les dépendances
pip install -r requirements.txt

# Tester les scripts
python script_etudiant_a.py
python script_etudiant_b.py
```

## Vérification des résultats

- Les scripts doivent s'exécuter sans erreur
- Les résultats doivent être identiques
- Les versions des packages sont exactement les mêmes

## Alternatives

- `pipreqs .` - génère requirements.txt basé sur les imports du projet
- `poetry export -f requirements.txt` - si utilisation de Poetry
