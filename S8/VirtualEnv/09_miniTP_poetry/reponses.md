# Réponses TP9 Poetry

## Installation de poetry

```bash
curl -sSL https://install.python-poetry.org | python3 -
```

Ajouter au ~/.bashrc:
```bash
export PATH="/home/b/c/_______/.local/bin:$PATH"
```

Puis:
```bash
source ~/.bashrc
which poetry  # Vérifie la localisation
```

## Exécution du code

### Duplication et initialisation
```bash
cp -r show_ade_source show_ade_test
cd show_ade_test
poetry init  # Appeler le package show_ade
```

### Premier test (échec attendu)
```bash
poetry run python -m show_ade 4YMHMH-120
```
→ Échec: module `show_ade` non trouvé car pas installé

### Installation avec Poetry
```bash
poetry install
```

### Packages manquants
En analysant le code source, les dépendances nécessaires sont:
- **requests** (pour les appels HTTP)
- **ics** (pour parser les fichiers calendrier)
- **rich** (pour l'affichage en table)

### Ajout des dépendances
```bash
poetry add requests ics rich
```

## Mode CLI

### Ajout dans pyproject.toml
```toml
[tool.poetry.scripts]
show_ade = "show_ade:main"
```

### Réinstaller et tester
```bash
poetry install
poetry run show_ade 4YMHMH-120
```

## Installation locale (wheel)

### Construction du wheel
```bash
poetry build
```

### Création venv et installation
```bash
python -m venv env_local
source env_local/bin/activate
pip install dist/show_ade-*.whl
```

### Utilisation en CLI
```bash
show_ade 4YMHMH-120
```

**Note**: Les dépendances (requests, ics, rich) s'installent automatiquement avec le wheel - c'est l'avantage de Poetry sur pip freeze!
