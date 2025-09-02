# 📅 Synchronisation API vers Google Calendar

Script Python permettant de synchroniser automatiquement des événements depuis une API externe vers un calendrier Google. Compatible avec l'exécution automatisée via CRON.

## 📋 Prérequis

- Python 3.7 ou supérieur
- Compte Google avec accès à Google Calendar
- Accès à l'API source des événements

## 🚀 Installation

### 1. Cloner/télécharger le projet

```bash
git clone https://github.com/PolySim/IS2A.git
cd EDT
```

### 2. Installer les dépendances Python

```bash
pip install google-auth google-auth-oauthlib google-auth-httplib2 google-api-python-client requests
```

Ou via requirements.txt :

```bash
pip install -r requirements.txt
```

### 3. Configuration Google Calendar API

#### 3.1 Créer un projet Google Cloud

1. Allez sur [Google Cloud Console](https://console.cloud.google.com/)
2. Créez un nouveau projet ou sélectionnez un projet existant
3. Notez l'ID du projet pour référence

#### 3.2 Activer l'API Google Calendar

1. Dans le menu latéral : **"APIs et services"** > **"Bibliothèque"**
2. Recherchez **"Google Calendar API"**
3. Cliquez sur **"Activer"**

#### 3.3 Configurer l'écran de consentement OAuth

1. **"APIs et services"** > **"Écran de consentement OAuth"**
2. Sélectionnez **"Usage externe"** (même pour usage personnel)
3. Remplissez les champs obligatoires :
   - **Nom de l'application** : "Calendar Sync" (ou votre choix)
   - **E-mail d'assistance utilisateur** : votre email
   - **E-mail de contact du développeur** : votre email
4. Cliquez **"Enregistrer et continuer"**

#### 3.4 Ajouter les portées (scopes)

1. Section **"Portées"** > **"Ajouter ou supprimer des portées"**
2. Ajoutez : `https://www.googleapis.com/auth/calendar`
3. **"Enregistrer et continuer"**

#### 3.5 Ajouter des utilisateurs de test

1. Section **"Utilisateurs de test"** > **"Ajouter des utilisateurs"**
2. Ajoutez votre adresse Gmail/Google
3. **"Enregistrer et continuer"**

#### 3.6 Créer les identifiants OAuth

1. **"APIs et services"** > **"Identifiants"** > **"Créer des identifiants"**
2. Sélectionnez **"ID client OAuth 2.0"**
3. Type d'application : **"Application de bureau"**
4. Nom : "Calendar Sync Client" (ou votre choix)
5. **"Créer"**
6. **Téléchargez le fichier JSON** et renommez-le `credentials.json`
7. Placez `credentials.json` dans le répertoire du projet

## ⚙️ Configuration

### 1. Configuration de l'API source

Créez un fichier `.env` dans le répertoire du projet :

```bash
# URL de votre API (remplacez par la vraie URL)
API_URL=https://votre-api.polytech.com/events

# Si votre API nécessite une authentification, ajoutez :
# API_TOKEN=votre-token-si-necessaire
```

### 2. Adaptation du script (si nécessaire)

Si la structure de votre API diffère, modifiez la fonction `normalize_event()` dans `calendar_sync.py` :

```python
def normalize_event(self, api_event):
    # Adaptez selon la structure de votre API
    # Actuellement configuré pour :
    # {
    #   "title": "Titre de l'événement",
    #   "description": "Description",
    #   "start": "20250901T101500Z",
    #   "end": "20250901T111500Z"
    # }
```

## 🔧 Première utilisation

### 1. Test de l'authentification

```bash
python3 calendar_sync.py
```

**⚠️ Important** : La première exécution ouvrira automatiquement votre navigateur web pour l'authentification Google.

1. **Connectez-vous** avec votre compte Google
2. **Autorisez l'application** (vous verrez un avertissement "App not verified")
3. Cliquez sur **"Advanced"** puis **"Go to Calendar Sync (unsafe)"**
4. Accordez les permissions pour accéder à votre calendrier

### 2. Vérification

Si tout fonctionne, vous devriez voir :

```
2025-09-02 12:06:23 - INFO - Récupéré X événements depuis l'API pour 2025-09-02 à 2025-10-02
2025-09-02 12:06:24 - INFO - Événement créé: Nom de l'événement
2025-09-02 12:06:24 - INFO - Synchronisation terminée - Créés: X, Mis à jour: 0, Supprimés: 0, Erreurs: 0
```

## 🤖 Automatisation avec CRON

### 1. Configuration des variables d'environnement pour CRON

```bash
# Éditez le crontab
crontab -e

# Ajoutez les variables d'environnement en haut du fichier
API_URL=https://votre-api.polytech.com/events

# Puis ajoutez la tâche CRON (exemples) :
```

### 2. Exemples de planification CRON

```bash
# Synchronisation toutes les heures
0 * * * * cd /chemin/vers/votre/projet && /usr/bin/python3 calendar_sync.py

# Synchronisation toutes les 30 minutes
*/30 * * * * cd /chemin/vers/votre/projet && /usr/bin/python3 calendar_sync.py

# Synchronisation tous les matins à 8h
0 8 * * * cd /chemin/vers/votre/projet && /usr/bin/python3 calendar_sync.py

# Synchronisation du lundi au vendredi à 8h et 18h
0 8,18 * * 1-5 cd /chemin/vers/votre/projet && /usr/bin/python3 calendar_sync.py
```

### 3. Vérification des logs CRON

```bash
# Vérifier les logs du script
tail -f calendar_sync.log

# Vérifier les logs système de CRON
tail -f /var/log/cron.log    # Sur Ubuntu/Debian
tail -f /var/log/system.log  # Sur macOS
```

## 📁 Structure des fichiers

Après installation et première exécution :

```
votre-projet/
├── calendar_sync.py          # Script principal
├── credentials.json          # Identifiants Google (à garder secret)
├── token.pickle             # Token d'authentification (généré automatiquement)
├── calendar_sync_state.json # État de synchronisation (généré automatiquement)
├── calendar_sync.log        # Logs de synchronisation
├── .env                     # Variables d'environnement (optionnel)
└── README.md               # Ce fichier
```

## 🔍 Fonctionnalités

### ✅ Synchronisation intelligente

- **Création** : Nouveaux événements de l'API → Ajoutés au calendrier Google
- **Mise à jour** : Événements modifiés dans l'API → Mis à jour dans Google Calendar
- **Suppression** : Événements supprimés de l'API → Supprimés du calendrier Google

### ✅ Gestion des doublons

- Système de hash unique pour chaque événement
- Évite la création de doublons lors des synchronisations répétées
- Identification via marqueur `[API_SYNC_ID: xxx]` dans la description

### ✅ Robustesse

- Gestion automatique des tokens expirés
- Logs détaillés pour debugging
- Gestion des erreurs réseau et API
- Compatible avec l'exécution CRON (non-interactive)

## 🛠️ Configuration avancée

### Utiliser un calendrier spécifique

Pour synchroniser vers un calendrier autre que le principal :

1. **Récupérez l'ID du calendrier** :

   - Ouvrez [Google Calendar](https://calendar.google.com)
   - Cliquez sur les "3 points" à côté du calendrier souhaité
   - "Paramètres et partage" > "ID du calendrier"

2. **Modifiez le script** :

```python
# Remplacez dans __init__
sync = GoogleCalendarSync(calendar_id='votre-id-calendrier@group.calendar.google.com')
```

### Modifier la plage de synchronisation

Par défaut : 30 jours à partir d'aujourd'hui. Pour modifier :

```python
# Dans fetch_api_events(), changez :
end_date = (today + timedelta(days=60)).strftime('%Y-%m-%d')  # 60 jours au lieu de 30
```

### Headers API personnalisés

Si votre API nécessite des headers spéciaux :

```python
self.api_headers = {
    'Content-Type': 'application/json',
    'Authorization': 'Bearer YOUR_TOKEN',
    'X-API-Key': 'YOUR_API_KEY',
    'User-Agent': 'Calendar-Sync/1.0'
}
```

## 🐛 Dépannage

### Erreur "App not verified"

- **Normal pour les projets de développement**
- Cliquez "Advanced" puis "Go to [App Name] (unsafe)"
- Ou ajoutez votre email dans les "utilisateurs de test" (voir étape 3.5)

### Erreur "HTTPSConnectionPool"

- **Vérifiez l'URL de votre API** dans `.env` ou le script
- **Testez manuellement** : `curl "https://votre-api.com/events"`

### Erreur "Failed to resolve"

- **L'URL de l'API est incorrecte**
- Vérifiez l'orthographe et la disponibilité de l'API

### Token expiré

- **Supprimez** `token.pickle`
- **Relancez** le script pour ré-authentifier

### Problèmes de permissions CRON

```bash
# Vérifiez que le chemin Python est correct dans CRON
which python3

# Utilisez le chemin complet dans crontab
0 * * * * cd /chemin/vers/projet && /usr/bin/python3 calendar_sync.py
```

## 📊 Monitoring

### Vérifier les logs

```bash
# Logs en temps réel
tail -f calendar_sync.log

# Dernières 50 lignes
tail -50 calendar_sync.log

# Rechercher les erreurs
grep "ERROR" calendar_sync.log
```

### État de synchronisation

```bash
# Voir l'état actuel
cat calendar_sync_state.json | python3 -m json.tool
```

## 🔒 Sécurité

### Fichiers sensibles à protéger

- `credentials.json` : Identifiants Google (ne jamais partager)
- `token.pickle` : Token d'accès (ne jamais partager)
- `.env` : Variables d'environnement (peut contenir des tokens API)

### Permissions fichiers recommandées

```bash
chmod 600 credentials.json token.pickle .env
chmod 644 calendar_sync.py README.md
chmod 755 .
```

## 📞 Support

### Logs utiles pour le debugging

Fournissez toujours ces informations en cas de problème :

- Contenu du fichier `calendar_sync.log`
- Version de Python : `python3 --version`
- Structure d'un événement de votre API (anonymisé)
- Message d'erreur complet

### Structure attendue de l'API

Le script attend cette structure JSON :

```json
{
  "title": "Titre de l'événement",
  "description": "Description de l'événement",
  "start": "20250901T101500Z",
  "end": "20250901T111500Z"
}
```

### Format des dates

- **Format attendu** : `YYYYMMDDTHHMMSSZ` (exemple: `20250901T101500Z`)
- **Timezone** : Toutes les heures sont converties en Europe/Paris dans Google Calendar

---

## 🚀 Démarrage rapide

```bash
# 1. Installation des dépendances
pip install google-auth google-auth-oauthlib google-auth-httplib2 google-api-python-client requests

# 2. Configuration de l'URL API
echo "API_URL=https://votre-api.com/events" > .env

# 3. Première authentification
python3 calendar_sync.py

# 4. Configuration CRON (optionnel)
crontab -e
# Ajouter : 0 * * * * cd /chemin/vers/projet && python3 calendar_sync.py
```

**✨ C'est tout !** Vos événements seront automatiquement synchronisés.
