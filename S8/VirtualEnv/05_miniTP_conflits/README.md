# MiniTP Conflits

Pour comprendre les dépendances entre les paquets python, on va s'intéresser au package `myappconf` qui est actuellement dans le dossier `dist`

1. **Investiguez** sur les dépendances des packages du dossier `dist/`.
2. **Construisez** le graphe des dépendances correspondant.
3. Peut-on installer `myappconf` ?


## (BONUS) Quand vous serez utiliser un environnement virtuel

4. Comme on l'a fait juste avant, testez avec pip l'installation du paquet `myappconf`
```bash
pip install --no-index --find-links dist --dry-run myappconf
```

