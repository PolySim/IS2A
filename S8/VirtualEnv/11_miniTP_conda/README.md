# TP conda
L'objectif de ce TP est d'utiliser le script `plot_restaurants.py`

```bash
python plot_restaurants.py restaurants.json
```

# Utiliser un environnement conda

- Initialisez conda :

```bash
/usr/localTP/anaconda3/bin/conda init
```

- Vérifiez les environnements installés :

```bash
conda env list
```

Normalement, il devrait y avoir un package nommé ***mongoenv***.

- Activez l’environnement ***mongoenv*** :

```bash
conda activate mongoenv
```

- Testez le script `plot_restaurants.py`


```bash
python plot_restaurants.py restaurants.json
```

# (Bonus) Construction à la main


- Trouvez la localisation de l'environnement ***mongoenv*** en utilisant :

```bash
du -sh <dossier>
```

Normalement ça devrait faire beaucoup en taille. Est ce qu'on peut faire moins ? En effet ***mongoenv*** sera utilisé en NoSQL pour interfacer mongodb avec python, du coup peut être que des choses sont inutiles juste pour tester ce code.

- Construisez un environnement conda minimal pour tester le script `plot_restaurants.py`.

- Si vous n'y arrivez pas, aucun problème, on va essayer de faire autrement. Commencez par exporter dans un fichier yaml tout ce qui est installé dans ***mongoenv*** :

```bash
conda env export -n mongoenv > <votre_fichier>.yaml
```

- Modifier le code de votre nouveau fichier yaml en
    - mettant à jour `name : restoenv`
    - supprimant `prefix : ...`


- Essayer de créer un nouvel environnement conda à l'aide de votre fichier yaml :

```bash
conda env create -f <votre_fichier>.yaml
```

- Activez votre nouvel environnement et testez le script `plot_restaurants.py`.

La taille du dossier de l'environnement devrait faire la même taille qu'avant.

- Désactivez et supprimez votre nouvel environnement :
```bash
conda deactivate
conda env remove -n restoenv
```

- Supprimez des packages de votre fichier yaml (au minimum `pymongo`) et ressayer de l'installer, vérifiez que votre script fonctionne et regarder la taille de l'environnement. Recommencez jusqu'à ce que vous soyez satisfait du résultat.

Félicitations ! Vous avez créé un environnement plus léger, bravo, la planète vous dit merci... enfin presque.
Parce qu’en réalité, vous avez surtout cloné la plupart des composants de ***mongoenv***, et là où il existait déjà, vous en avez rajouté un nouveau.
Résultat : une noble intention, mais une famille entière de petits sapins vient d’y passer.
Que cette expérience vous serve de leçon... et souvenez-vous : un environnement réutilisé vaut mieux qu’un environnement recréé.
