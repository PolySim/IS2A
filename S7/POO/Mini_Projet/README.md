# Mini Projet POO

## Description

Projet Java avec interface graphique utilisant Swing.

## Prérequis

- Java JDK (version 8 ou supérieure)
- Un terminal/shell

## Lancer le projet

Pour compiler et exécuter le projet, placez-vous dans le dossier `src` et exécutez les commandes suivantes :

```bash
cd src
javac app/Main.java && java app/Main
```

Le programme lancera une fenêtre graphique de 1000x1000 pixels.

## Lancer les tests

Le projet utilise JUnit pour les tests unitaires.

### Étape 1 : Rendre le script exécutable

Avant la première exécution, vous devez rendre le script de test exécutable :

```bash
chmod +x run_test.sh
```

### Étape 2 : Exécuter les tests

Une fois le chmod effectué, lancez les tests avec :

```bash
./run_test.sh
```

Le script va :

1. Compiler les classes de jeu et les tests
2. Exécuter tous les tests avec JUnit Platform Console

## Structure du projet

```
Mini_Projet/
├── src/
│   ├── app/
│   │   ├── container/         # UI
│   │   └── Main.java          # Point d'entrée de l'application
│   ├── game/
│   │   ├── exceptions/        # Exceptions
│   │   └── composent/         # Composants du jeu
│   ├── tests/                 # Tests unitaires
│   └── lib/
│       └── junit-platform-console-standalone.jar
├── run_test.sh                # Script pour lancer les tests
└── README.md
```
