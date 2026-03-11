# miniTP Poetry

## Installation de poetry

- Installer `poetry`
```bash
curl -sSL https://install.python-poetry.org | python3 -
```

- Lors de l'installation, `poetry` vous rappelle : "Add `export PATH="/home/b/c/_______/.local/bin:$PATH"` to your shell configuration file." Ajoutez la ligne correspondante dans votre `.bashrc`

- Mettez à jour votre terminal en le fermant et le réouvrant ou en forçant la relecture de votre fichier `.bashrc` :
```bash
source ~/.bahsrc
```

- Testez avec la commande `which` la localisation du poetry que vous allez utiliser.


## Exécutez votre code

- Dupliquez le dossier show_ade_source (vous pouvez l'appeler par exemple show_ade_test)


- Allez dans votre nouveau dossier et **initialiser** votre répertoire avec `poetry` (appelez votre package `show_ade` et ensuite vous pouvez tout valider :wink:)

- Testez votre code en essayant la commande suivante :
```bash
poetry run python -m show_ade 4YMHMH-120
```
Normalement, la console devrait vous dire qu'il ne connaît pas le module `show_ade`. Bin oui, on a oublié de l'installer ! :sweat_smile:

- **Installez** avec `poetry` et retestez votre code. Est ce qu'il ne manquerait pas quelques packages ? :confused:

- Trouvez les packages à **ajouter** (en utilisant `poetry`) pour que votre code fonctionne.

## En mode CLI

On voudrait utiliser notre code en CLI :
```bash
poetry run show_ade 4YMHMH-120
```

 - Ajoutez ce code à votre fichier `pyproject.toml` pour ajouter qu'en cas d'appel CLI, on utilise le main de notre application.

```toml
[tool.poetry.scripts]
show_ade = "show_ade:main"

```

- **Installez** de nouveau votre package avec `poetry` et essayez votre commande en CLI.

### Installation en local

- **Construisez** un `wheel` de votre projet en utilisant `poetry`.

- Créez un environnement local avec venv et activez le.

- Installer localement votre package en utilisant la commande suivante :
```bash
pip install dist/show_ade-*.whl
```

- Vous pouvez utiliser votre commande sans passer par l'environnement de poetry.
```bash
show_ade 4YMHMH-120
````

Avez-vous remarqué que vous n'aviez pas eu besoin de gérer les dépendances de votre package à l'installation avec pip et que toutes les dépendances s'étaient installées automatiquement ? :sunglasses:
