# MiniTP Dependance

Pour comprendre les dépendances entre les paquets python, on va s'intéresser au package `myapp` qui est actuellement dans le dossier `dist`

- Commencez par regarder les fichiers dans le dossier `dist`. Vous devriez vous un ensemble de fichier `wheel` (repérable grâce à l'extension `.whl`).

    Chaque nom d'un fichier `wheel` a toujours la même nomenclature : `{nom_du_paquet}-{version_semver}-{autres_infos}.whl` (attention comme le caractère `-` délimite les infos, dans le nom du paquet le caractère `_` est utilisé pour les noms de paquet au lieu du `-`).

    La question est maintenant simple, est ce que certains de ces paquets sont nécessaires pour installer `myapp` ?

- Commencez par regarder si le paquet `dist/myapp-0.1.0-py3-none-any.whl` dépend d'autres paquets


```bash
unzip -p dist/myapp-0.1.0-py3-none-any.whl '*.dist-info/METADATA' | grep -E '^Requires-Dist:'
```

(le fichier `.whl` est un fichier zip qui garde un ensemble d'info dont la liste des dépendances dans le fichier `dist-info/METADATA` au niveau des champs  qui commencent par `Requires-Dist:`)

- Vérifiez que vous avez la ligne suivante :

```bash
Requires-Dist: pkg-d
```

- Comme `myapp` dépend de `pkg-d`, on va alors regarder si `pkg-d` dépend d'un autre package : vérifiez les dépendances présent dans le fichier `dist/pkg_d-0.1.0-py3-none-any.whl` (`pkg-d` correspond `dist/pkg_d.*.whl` voir remarque sur la nomenclature des fichiers `wheel`).

```bash
unzip -p dist/pkg_d-0.1.0-py3-none-any.whl '*.dist-info/METADATA' | grep -E '^Requires-Dist:'
```

- Construisez le graphe des dépendances où les noeuds sont les paquets nécessaires pour installer `myapp` et un arc d'un noeud de `A` vers `B` si on a besoin de `B` pour installer `A`.

- Trouvez un ordre sur les paquets pour installer `myapp`.

## (BONUS) Quand vous serez utiliser un environnement virtuel

- Testez l'installation (`--dry-run` teste juste sans vraiment installer) de votre package `myapp` avec `pip` :

```bash
pip install --no-index --find-links dist --dry-run myapp
```




<!--  -->
