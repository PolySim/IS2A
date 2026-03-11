# Réponses TP3 Pip

## 1. Qu'est-ce que PyPI ?
PyPI (Python Package Index) est le dépôt officiel de packages Python. Il contient plus de 400 000 packages accessibles via pip.

## 2. Vérifier la présence de pip
```bash
pip --version
# ou
pip3 --version
```

## 3. Trouver la version de pip
```bash
pip --version
# Affiche: pip X.X.X from /chemin/vers/pip (python X.X)
```

## 4. Lister les commandes pip
```bash
pip help
```

## 5. Liste des packages installés
```bash
pip list
```

## 6. Différence entre pip list et pip freeze
- `pip list` : Affiche tous les packages installés avec leurs versions (format lisible)
- `pip freeze` : Affiche les packages au format requirements.txt (==version)

**Difference** : `pip freeze` est conçu pour générer des fichiers requirements.txt utilisables pour reproduire l'environnement.
