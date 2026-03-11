# Réponses TP2 Path

## 1. Afficher la variable d'environnement PATH
```bash
echo $PATH
```

## 2. Ajouter un chemin vers le dossier Téléchargements
```bash
export PATH="$PATH:/chemin/vers/Téléchargements"
```

## 3. Revérifier le PATH
```bash
echo $PATH
```

## 4. Fermer le terminal
## 5. Rouvrir le terminal
Le PATH a été réinitialisé - les modifications temporaires sont perdues.

## 6. Ouvrir ~/.bashrc et ajouter la commande du point 2
Ajouter `export PATH="$PATH:/chemin/vers/Téléchargements"` à la fin du fichier ~/.bashrc

## 7. Rouvrir le terminal
Le chemin est maintenant persistsistant car il est chargé à chaque ouverture du terminal via .bashrc
