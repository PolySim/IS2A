# Réponses TP11 Conda

## Objectif
Exécuter le script `plot_restaurants.py`

```bash
python plot_restaurants.py restaurants.json
```

## Utiliser un environnement conda

### Initialiser conda
```bash
/usr/localTP/anaconda3/bin/conda init
```

### Vérifier les environnements installés
```bash
conda env list
```
→ Devrait montrer l'environnement **mongoenv**

### Activer mongoenv
```bash
conda activate mongoenv
```

### Tester le script
```bash
python plot_restaurants.py restaurants.json
```

## (Bonus) Construction à la main

### Localiser mongoenv
```bash
du -sh <dossier>
```

L'environnement est volumineux car il contient de nombreuses dépendances pour MongoDB.

### Construire un environnement minimal

1. **Exporter mongoenv** :
```bash
conda env export -n mongoenv > mongoenv.yaml
```

2. **Modifier le fichier YAML** :
- Changer `name: mongoenv` en `name: restoenv`
- Supprimer la ligne `prefix: ...`

3. **Créer le nouvel environnement** :
```bash
conda env create -f mongoenv.yaml
```

4. **Tester et réduire** :
```bash
conda activate restoenv
python plot_restaurants.py restaurants.json
conda deactivate
conda env remove -n restoenv
```

5. **Supprimer des packages inutiles** (ex: pymongo) et recréer jusqu'à obtenir un environnement minimal.

## Leçon importante

Un environnement réutilisé > environnement recréé

Créer des environnements légers et les partager est écologique, mais dupliquer un environnement existant augmente lempreinte disque. Privilégier:
- Les' environnements partagés
- poetry/venv pour les petits projets
- conda uniquement quand des dépendances système sont nécessaires
