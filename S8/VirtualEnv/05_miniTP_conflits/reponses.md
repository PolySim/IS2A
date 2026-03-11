# Réponses TP5 Conflits

## 1. Investigation des dépendances

- **myappconf** dépend de: pkg-a, pkg-b
- **pkg-a** dépend de: pkg-c (<2)
- **pkg-b** dépend de: pkg-c (>=2.0.0)

## 2. Graphe des dépendances

```
myappconf
  ├── pkg_a --> pkg_c (<2)
  └── pkg_b --> pkg_c (>=2.0.0)
```

## 3. Peut-on installer myappconf ?

**NON - CONFLIT IMPOSSIBLE À RÉSOUDRE**

Il y a un conflit de version insoluble :

- pkg-a exige pkg-c en version **< 2** (inférieure à 2.0.0)
- pkg-b exige pkg-c en version **>= 2.0.0** (égale ou supérieure à 2.0.0)

Ces deux exigences sont mutuellement exclusives - il n'existe aucune version de pkg-c qui satisfies les deux contraintes simultanément.

## (BONUS) Test d'installation

```bash
pip install --no-index --find-links dist --dry-run myappconf
```

Résultat: Échec - conflit de dépendances détecté.
