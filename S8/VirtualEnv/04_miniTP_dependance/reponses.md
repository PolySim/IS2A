# Réponses TP4 Dépendances

## Graphe des dépendances

```
myapp
  └── pkg_d
        ├── pkg_a
        │     └── pkg_b
        │           ├── pkg_e
        │           └── pkg_f
        └── pkg_c
              ├── pkg_a
              ├── pkg_b
              │     ├── pkg_e
              │     └── pkg_f
              └── pkg_f
```

## Dépendances détaillées

- **myapp** dépend de: pkg_d
- **pkg_d** dépend de: pkg_a, pkg_c
- **pkg_a** dépend de: pkg_b
- **pkg_b** dépend de: pkg_e, pkg_f
- **pkg_c** dépend de: pkg_a, pkg_b, pkg_f
- **pkg_e** n'a aucune dépendance
- **pkg_f** n'a aucune dépendance

## Ordre d'installation (du plus indépendant au plus dépendé)

1. **pkg_e** (pas de dépendances)
2. **pkg_f** (pas de dépendances)
3. **pkg_b** (dépend de pkg_e et pkg_f)
4. **pkg_a** (dépend de pkg_b)
5. **pkg_c** (dépend de pkg_a, pkg_b, pkg_f)
6. **pkg_d** (dépend de pkg_a et pkg_c)
7. **myapp** (dépend de pkg_d)

## (BONUS) Test d'installation

```bash
pip install --no-index --find-links dist --dry-run myapp
```
