Avant l'incrémentation

- **Père** : `ent = 0`
- **Fils** : `ent = 0`

Lors de l'appel à `fork()`, le processus fils est créé comme une copie exacte du processus père. La variable `ent` (initialisée à 0) est dupliquée dans l'espace mémoire du fils. Chaque processus possède donc sa propre variable.

Après l'incrémentation

- **Père** : `ent = 2` (incrémenté par `ent = ent + 2`)
- **Fils** : `ent = 1` (incrémenté par `ent = ent + 1`)

Justification : Les deux processus modifient leur propre copie de la variable `ent` :

- Le père ajoute 2 à sa copie (0 + 2 = 2)
- Le fils ajoute 1 à sa copie (0 + 1 = 1)

Les modifications sont indépendantes car les processus ont des espaces mémoire séparés. Il n'y a pas de partage de mémoire entre père et fils.
