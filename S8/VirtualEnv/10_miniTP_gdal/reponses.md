# Réponses TP10 Gdal

## Problème avec l'installation de osgeo/gdal

Le package `osgeo` (qui contient GDAL) ne peut **PAS** être installé simplement avec pip ou venv.

## Pourquoi ?

GDAL est une **bibliothèque système** qui nécessite:
1. L'installation des libraries C/C++ natives sur le système
2. Des bindings Python compilés

Ce n'est pas un package Python pur - c'est un wrapper autour de la bibliothèque C++ GDAL.

## Solutions possibles

### Option 1: Conda/Mamba (recommandé)
```bash
conda install gdal
# ou
mamba install gdal
```

### Option 2: Installation système + pip
1. Installer GDAL système:
   - **Linux (Debian/Ubuntu)**: `sudo apt-get install libgdal-dev`
   - **macOS**: `brew install gdal`
   - **Windows**: Installer depuis OSGeo4W ou conda

2. Ensuite installer le package Python:
```bash
pip install pygdal
```

### Option 3: Docker
Utiliser une image Docker avec GDAL pré-installé.

### Option 4: poetry avec pip
```bash
poetry add numpy
poetry run pip install GDAL==version_system
```

## Résumé

venv et poetry gèrent uniquement les packages Python purs. Pour GDAL, il faut:
1. Installer la bibliothèque système (géographique)
2. Installer le package Python correspondant à la version système

C'est pourquoi conda/mamba sont préférés pour les packages scientifiques géospatiaux.
