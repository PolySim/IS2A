# miniTP Poetry

## Exécutez votre code

- Dupliquez le dossier `show_ade_source/` (vous pouvez l'appeler par exemple `show_ade_test/`)

```bash
cp -r show_ade_source/ show_ade_test/
```

- Allez dans votre nouveau dossier `show_ade_test/` et **initialiser** votre répertoire avec `poetry` (appelez votre package `show_ade`, utiliser `^3.09` pour la "Compatible Python versions" et ensuite vous pouvez tout valider :wink:)

- Testez votre code en essayant la commande suivante :

```bash
poetry run python -m show_ade 4YMHMH-120
```

Normalement, ça devrait fonctionner ! On va essayer de comprendre ce que poetry a fait.

- Activez l'environnement créé par poetry :

```bash
poetry shell
```

Vous devriez avoir le préfix "(show-ade-py3.11)" dans votre prompt (ou un truc du genre).

- Regardez la liste des packages python installé

```bash
pip freeze
```

Vous devriez voir `requests`, `ics` et `rich` qui ne sont pas installé de base. Poetry est allé les installer directement à l'initialisation

- Quitter l'environnement virtuel de poetry :

```bash
exit
```

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
```

Avez-vous remarqué que vous n'aviez pas eu besoin de gérer les dépendances de votre package à l'installation avec pip et que toutes les dépendances s'étaient installées automatiquement ? :sunglasses:
