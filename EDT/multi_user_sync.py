#!/usr/bin/env python3
"""
Script de synchronisation multi-utilisateurs OPTIMISÉ
Reprend la logique d'optimisation du script mono-utilisateur
"""

import os
import json
import sqlite3
import pickle
import requests
import time
import threading
from datetime import datetime, timedelta
from google.auth.transport.requests import Request
from google_auth_oauthlib.flow import InstalledAppFlow
from googleapiclient.discovery import build
from googleapiclient.errors import HttpError
import hashlib
import logging
from typing import Dict, List, Optional, Tuple
import pytz

try:
    with open('.env', 'r') as f:
        for line in f:
            if line.strip() and not line.startswith('#'):
                key, value = line.strip().split('=', 1)
                os.environ[key] = value
except FileNotFoundError:
    pass

# Configuration du logging
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(levelname)s - %(message)s',
    handlers=[
        logging.FileHandler('multi_user_calendar_sync.log'),
        logging.StreamHandler()
    ]
)
logger = logging.getLogger(__name__)

class RateLimiter:
    """Gestionnaire de rate limiting pour l'API Google Calendar (600 requêtes/minute)"""
    
    def __init__(self, max_requests_per_minute=600):
        self.max_requests = max_requests_per_minute
        self.request_times = []
        self.lock = threading.Lock()
        self.total_requests = 0
        
    def wait_if_needed(self):
        """Attend si nécessaire pour respecter le rate limit"""
        with self.lock:
            now = time.time()
            
            # Nettoie les requêtes de plus d'une minute
            cutoff_time = now - 60
            self.request_times = [t for t in self.request_times if t > cutoff_time]
            
            # Si on approche de la limite, on attend
            if len(self.request_times) >= self.max_requests:
                # Calcule le temps d'attente nécessaire
                oldest_request = min(self.request_times)
                wait_time = 60 - (now - oldest_request) + 1  # +1 seconde de marge
                
                if wait_time > 0:
                    logger.warning(f"Rate limit atteint, attente de {wait_time:.1f} secondes...")
                    time.sleep(wait_time)
                    # Recalcule après l'attente
                    now = time.time()
                    cutoff_time = now - 60
                    self.request_times = [t for t in self.request_times if t > cutoff_time]
            
            # Enregistre cette requête
            self.request_times.append(now)
            self.total_requests += 1
            
            # Log périodique des statistiques
            if self.total_requests % 50 == 0:
                logger.info(f"Rate limiter: {len(self.request_times)} requêtes dans la dernière minute (total: {self.total_requests})")
    
    def get_stats(self):
        """Retourne les statistiques du rate limiter"""
        with self.lock:
            now = time.time()
            cutoff_time = now - 60
            recent_requests = len([t for t in self.request_times if t > cutoff_time])
            return {
                'requests_last_minute': recent_requests,
                'total_requests': self.total_requests,
                'remaining_capacity': max(0, self.max_requests - recent_requests)
            }

class MultiUserCalendarSync:
    def __init__(self, db_path='users_sync.db'):
        self.db_path = db_path
        self.scopes = ['https://www.googleapis.com/auth/calendar']
        
        # Configuration API
        self.api_url_template = os.getenv('API_URL', 'https://votre-api.com/events?user={user_id}')
        self.api_headers = {
            'Content-Type': 'application/json',
            'Cookie': os.getenv('COOKIE')
        }
        
        # Rate limiter pour l'API Google Calendar
        self.rate_limiter = RateLimiter(max_requests_per_minute=550)  # Marge de sécurité
        
        # Initialisation de la base de données
        self.init_database()
    
    def make_api_call(self, api_func, *args, **kwargs):
        """
        Exécute un appel API avec gestion du rate limiting et retry automatique
        """
        max_retries = 3
        base_delay = 1
        
        for attempt in range(max_retries + 1):
            try:
                # Respecte le rate limit avant chaque appel
                self.rate_limiter.wait_if_needed()
                
                # Exécute l'appel API
                return api_func(*args, **kwargs).execute()
                
            except HttpError as e:
                if e.resp.status == 429:  # Too Many Requests
                    if attempt < max_retries:
                        # Backoff exponentiel
                        delay = base_delay * (2 ** attempt)
                        logger.warning(f"Erreur 429 (Too Many Requests), retry dans {delay}s (tentative {attempt + 1}/{max_retries + 1})")
                        time.sleep(delay)
                        continue
                    else:
                        logger.error("Nombre maximum de tentatives atteint pour l'erreur 429")
                        raise
                elif e.resp.status in [500, 502, 503, 504]:  # Erreurs serveur
                    if attempt < max_retries:
                        delay = base_delay * (2 ** attempt)
                        logger.warning(f"Erreur serveur {e.resp.status}, retry dans {delay}s (tentative {attempt + 1}/{max_retries + 1})")
                        time.sleep(delay)
                        continue
                    else:
                        logger.error(f"Erreur serveur persistante {e.resp.status}")
                        raise
                else:
                    # Autres erreurs HTTP, pas de retry
                    raise
            except Exception as e:
                # Autres exceptions, pas de retry
                raise
        
        return None
    
    def init_database(self):
        """Initialise la base de données SQLite pour stocker les utilisateurs et leurs tokens"""
        conn = sqlite3.connect(self.db_path)
        cursor = conn.cursor()
        
        # Table utilisateurs
        cursor.execute('''
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                email TEXT UNIQUE NOT NULL,
                user_api_id TEXT UNIQUE NOT NULL,
                calendar_id TEXT DEFAULT 'primary',
                credentials_data BLOB,
                last_sync TIMESTAMP,
                active BOOLEAN DEFAULT 1,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        ''')
        
        # Table état des événements par utilisateur
        cursor.execute('''
            CREATE TABLE IF NOT EXISTS sync_states (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER,
                event_hash TEXT NOT NULL,
                google_event_id TEXT,
                summary TEXT,
                last_sync TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (user_id) REFERENCES users (id),
                UNIQUE(user_id, event_hash)
            )
        ''')
        
        conn.commit()
        conn.close()
        logger.info("Base de données initialisée")
    
    def add_user(self, email: str, user_api_id: str, calendar_id: str = 'primary') -> bool:
        """Ajoute un nouvel utilisateur et effectue l'authentification"""
        try:
            # Authentification Google pour ce nouvel utilisateur
            if not os.path.exists('credentials.json'):
                logger.error("Fichier credentials.json manquant")
                return False
            
            flow = InstalledAppFlow.from_client_secrets_file(
                'credentials.json', self.scopes)
            creds = flow.run_local_server(
                port=0,
                prompt='consent',
                access_type='offline'
            )
            
            # Sauvegarde en base de données
            conn = sqlite3.connect(self.db_path)
            cursor = conn.cursor()
            
            credentials_data = pickle.dumps(creds)
            
            cursor.execute('''
                INSERT OR REPLACE INTO users 
                (email, user_api_id, calendar_id, credentials_data, last_sync)
                VALUES (?, ?, ?, ?, ?)
            ''', (email, user_api_id, calendar_id, credentials_data, None))
            
            conn.commit()
            user_id = cursor.lastrowid
            conn.close()
            
            logger.info(f"Utilisateur {email} ajouté avec succès (ID: {user_id})")
            return True
            
        except Exception as e:
            logger.error(f"Erreur lors de l'ajout de l'utilisateur {email}: {e}")
            return False
    
    def get_active_users(self) -> List[Dict]:
        """Récupère tous les utilisateurs actifs"""
        conn = sqlite3.connect(self.db_path)
        cursor = conn.cursor()
        
        cursor.execute('''
            SELECT id, email, user_api_id, calendar_id, credentials_data, last_sync
            FROM users 
            WHERE active = 1
        ''')
        
        users = []
        for row in cursor.fetchall():
            users.append({
                'id': row[0],
                'email': row[1],
                'user_api_id': row[2],
                'calendar_id': row[3],
                'credentials_data': row[4],
                'last_sync': row[5]
            })
        
        conn.close()
        return users
    
    def authenticate_user(self, credentials_data: bytes) -> Optional[any]:
        """Authentifie un utilisateur avec ses credentials stockés"""
        try:
            creds = pickle.loads(credentials_data)
            
            # Vérification et refresh du token si nécessaire
            if not creds.valid:
                if creds.expired and creds.refresh_token:
                    creds.refresh(Request())
                    # Sauvegarder les nouveaux credentials
                    # TODO: Vous pourriez vouloir mettre à jour la DB ici
                else:
                    logger.error("Token expiré sans refresh token disponible")
                    return None
            
            return build('calendar', 'v3', credentials=creds)
            
        except Exception as e:
            logger.error(f"Erreur d'authentification utilisateur: {e}")
            return None
    
    def fetch_api_events_for_user(self, user_api_id: str) -> List[Dict]:
        """Récupération des événements pour un utilisateur spécifique"""
        try:
            today = datetime.now()
            start_date = today.strftime('%Y-%m-%d')
            end_date = (today + timedelta(days=30)).strftime('%Y-%m-%d')
            
            # Construction de l'URL avec l'ID utilisateur
            url = self.api_url_template.format(user_id=user_api_id)
            url += f"&start={start_date}&end={end_date}"
            
            response = requests.get(url, headers=self.api_headers, timeout=30)
            response.raise_for_status()
            
            events = response.json()
            logger.info(f"Récupéré {len(events)} événements pour l'utilisateur {user_api_id}")
            return events
            
        except requests.RequestException as e:
            logger.error(f"Erreur lors de la récupération des événements pour {user_api_id}: {e}")
            return []
    
    def normalize_event(self, api_event: Dict, user_id: int) -> Tuple[Optional[Dict], Optional[str]]:
        """Normalise un événement en incluant l'ID utilisateur dans le hash"""
        try:
            paris_tz = pytz.timezone('Europe/Paris')
            
            # Conversion du format datetime de l'API (20250901T101500Z) 
            # Ces dates sont déjà en heure française locale
            start_dt_naive = datetime.strptime(api_event['start'], '%Y%m%dT%H%M%SZ')
            end_dt_naive = datetime.strptime(api_event['end'], '%Y%m%dT%H%M%SZ')
            
            # Assignation du fuseau horaire français aux objets datetime
            start_dt_paris = paris_tz.localize(start_dt_naive)
            end_dt_paris = paris_tz.localize(end_dt_naive)
            
            # Format ISO 8601 pour Google Calendar (avec le fuseau horaire local)
            start_iso = start_dt_paris.strftime('%Y-%m-%dT%H:%M:%S')
            end_iso = end_dt_paris.strftime('%Y-%m-%dT%H:%M:%S')
            
            event = {
                'summary': api_event.get('title', 'Événement sans titre').strip(),
                'description': api_event.get('description', '').strip(),
                'start': {
                    'dateTime': start_iso,
                    'timeZone': 'Europe/Paris',
                },
                'end': {
                    'dateTime': end_iso,
                    'timeZone': 'Europe/Paris',
                }
            }
            
            # Hash unique incluant l'ID utilisateur pour éviter les conflits
            event_content = f"{user_id}{api_event.get('title', '')}{api_event.get('start', '')}{api_event.get('end', '')}"
            event_hash = hashlib.md5(event_content.encode()).hexdigest()
            
            event['description'] += f"\n\n[USER_SYNC_ID: {user_id}_{event_hash}]"
            
            return event, event_hash
            
        except Exception as e:
            logger.error(f"Erreur lors de la normalisation de l'événement: {e}")
            return None, None
    
    def get_user_sync_state(self, user_id: int) -> Dict[str, Dict]:
        """Récupère l'état de synchronisation pour un utilisateur"""
        conn = sqlite3.connect(self.db_path)
        cursor = conn.cursor()
        
        cursor.execute('''
            SELECT event_hash, google_event_id, summary
            FROM sync_states
            WHERE user_id = ?
        ''', (user_id,))
        
        state = {}
        for row in cursor.fetchall():
            state[row[0]] = {
                'google_id': row[1],
                'summary': row[2]
            }
        
        conn.close()
        return state
    
    def update_sync_state(self, user_id: int, event_hash: str, google_event_id: str, summary: str):
        """Met à jour l'état de synchronisation"""
        conn = sqlite3.connect(self.db_path)
        cursor = conn.cursor()
        
        cursor.execute('''
            INSERT OR REPLACE INTO sync_states 
            (user_id, event_hash, google_event_id, summary, last_sync)
            VALUES (?, ?, ?, ?, ?)
        ''', (user_id, event_hash, google_event_id, summary, datetime.now()))
        
        conn.commit()
        conn.close()
    
    def remove_sync_state(self, user_id: int, event_hash: str):
        """Supprime un événement de l'état de synchronisation"""
        conn = sqlite3.connect(self.db_path)
        cursor = conn.cursor()
        
        cursor.execute('''
            DELETE FROM sync_states 
            WHERE user_id = ? AND event_hash = ?
        ''', (user_id, event_hash))
        
        conn.commit()
        conn.close()
    
    def delete_event(self, service, calendar_id: str, event_id: str) -> bool:
        """Suppression d'un événement"""
        try:
            self.make_api_call(
                service.events().delete,
                calendarId=calendar_id,
                eventId=event_id
            )
            
            logger.info(f"Événement supprimé : {event_id}")
            return True
            
        except HttpError as e:
            logger.error(f"Erreur lors de la suppression de l'événement : {e}")
            return False
    
    def create_event(self, service, calendar_id: str, event: Dict) -> Optional[Dict]:
        """Création d'un nouvel événement"""
        try:
            created_event = self.make_api_call(
                service.events().insert,
                calendarId=calendar_id,
                body=event
            )
            
            logger.info(f"Événement créé : {event['summary']}")
            return created_event
            
        except HttpError as e:
            logger.error(f"Erreur lors de la création de l'événement : {e}")
            return None
    
    def sync_user_events(self, user: Dict) -> Dict[str, int]:
        """
        Synchronise les événements pour un utilisateur spécifique
        Reprend la logique optimisée du script mono-utilisateur
        """
        stats = {'created': 0, 'updated': 0, 'deleted': 0, 'unchanged': 0, 'errors': 0}
        
        try:
            # Authentification
            service = self.authenticate_user(user['credentials_data'])
            if not service:
                logger.error(f"Échec de l'authentification pour {user['email']}")
                stats['errors'] += 1
                return stats
            
            # Récupération des événements API
            api_events = self.fetch_api_events_for_user(user['user_api_id'])
            if not api_events:
                logger.warning(f"Aucun événement pour {user['email']}")
                return stats
            
            # Chargement de l'état précédent
            previous_state = self.get_user_sync_state(user['id'])
            
            # Normalisation des événements de l'API pour créer un dictionnaire de référence
            api_events_dict = {}
            for api_event in api_events:
                event, event_hash = self.normalize_event(api_event, user['id'])
                if event and event_hash:
                    api_events_dict[event_hash] = {
                        'event': event,
                        'summary': event['summary'],
                        'last_sync': datetime.utcnow().isoformat()
                    }
            
            # ÉTAPE 1: Nettoyer les anciens événements
            logger.info(f"Étape 1: Nettoyage des anciens événements pour {user['email']}...")
            for old_hash, old_data in previous_state.items():
                if old_hash not in api_events_dict:
                    # L'événement n'est plus dans l'API -> le supprimer
                    if 'google_id' in old_data and old_data['google_id']:
                        if self.delete_event(service, user['calendar_id'], old_data['google_id']):
                            stats['deleted'] += 1
                            self.remove_sync_state(user['id'], old_hash)
                            logger.info(f"Supprimé: {old_data.get('summary', 'Événement sans titre')}")
                        else:
                            stats['errors'] += 1
                else:
                    # L'événement existe encore dans l'API
                    # Vérifier s'il a changé en comparant les résumés
                    api_summary = api_events_dict[old_hash]['summary']
                    old_summary = old_data.get('summary', '')
                    
                    if api_summary != old_summary:
                        # L'événement a changé -> le supprimer (il sera recréé à l'étape 2)
                        if 'google_id' in old_data and old_data['google_id']:
                            if self.delete_event(service, user['calendar_id'], old_data['google_id']):
                                stats['updated'] += 1  # Compte comme une mise à jour
                                self.remove_sync_state(user['id'], old_hash)
                                logger.info(f"Modifié (suppression): {old_summary} -> {api_summary}")
                            else:
                                stats['errors'] += 1
                    else:
                        # L'événement n'a pas changé -> le conserver
                        stats['unchanged'] += 1
                        # Conserver l'ID Google pour éviter de le recréer
                        api_events_dict[old_hash]['google_id'] = old_data.get('google_id')
            
            # ÉTAPE 2: Ajouter les nouveaux événements
            logger.info(f"Étape 2: Ajout des nouveaux événements pour {user['email']}...")
            for event_hash, event_data in api_events_dict.items():
                if 'google_id' in event_data:
                    # L'événement existe déjà et n'a pas changé -> ne rien faire
                    continue
                
                # L'événement est nouveau ou a changé -> le créer
                created_event = self.create_event(service, user['calendar_id'], event_data['event'])
                if created_event:
                    if event_hash in previous_state:
                        # C'était une modification (déjà compté dans updated)
                        logger.info(f"Modifié (création): {event_data['summary']}")
                    else:
                        # C'est un nouvel événement
                        stats['created'] += 1
                        logger.info(f"Créé: {event_data['summary']}")
                    
                    # Mise à jour de l'état de synchronisation
                    self.update_sync_state(
                        user['id'], 
                        event_hash, 
                        created_event['id'], 
                        event_data['summary']
                    )
                else:
                    stats['errors'] += 1
            
            # Mise à jour de la dernière synchronisation
            conn = sqlite3.connect(self.db_path)
            cursor = conn.cursor()
            cursor.execute(
                'UPDATE users SET last_sync = ? WHERE id = ?',
                (datetime.now(), user['id'])
            )
            conn.commit()
            conn.close()
            
        except Exception as e:
            logger.error(f"Erreur lors de la synchronisation de {user['email']}: {e}")
            stats['errors'] += 1
        
        return stats
    
    def sync_all_users(self) -> bool:
        """Synchronise tous les utilisateurs actifs"""
        users = self.get_active_users()
        if not users:
            logger.warning("Aucun utilisateur actif trouvé")
            return False
        
        total_stats = {'created': 0, 'updated': 0, 'deleted': 0, 'unchanged': 0, 'errors': 0}
        
        for user in users:
            logger.info(f"Synchronisation de {user['email']} (API ID: {user['user_api_id']})")
            user_stats = self.sync_user_events(user)
            
            # Agrégation des statistiques
            for key in total_stats:
                total_stats[key] += user_stats[key]
            
            logger.info(f"Utilisateur {user['email']} - Créés: {user_stats['created']}, "
                       f"Mis à jour: {user_stats['updated']}, Supprimés: {user_stats['deleted']}, "
                       f"Inchangés: {user_stats['unchanged']}, Erreurs: {user_stats['errors']}")
        
        logger.info(f"Synchronisation globale terminée - Total: Créés: {total_stats['created']}, "
                   f"Mis à jour: {total_stats['updated']}, Supprimés: {total_stats['deleted']}, "
                   f"Inchangés: {total_stats['unchanged']}, Erreurs: {total_stats['errors']}")
        
        # Affichage des statistiques du rate limiter
        rate_stats = self.rate_limiter.get_stats()
        logger.info(f"Rate limiter - Requêtes dans la dernière minute: {rate_stats['requests_last_minute']}/{self.rate_limiter.max_requests}, "
                   f"Total requêtes: {rate_stats['total_requests']}, Capacité restante: {rate_stats['remaining_capacity']}")
        
        return True

def main():
    """Fonction principale avec gestion des commandes"""
    import sys
    
    sync = MultiUserCalendarSync()
    
    if len(sys.argv) < 2:
        print("Usage:")
        print("  python multi_user_sync.py add_user <email> <user_api_id> [calendar_id]")
        print("  python multi_user_sync.py sync")
        print("  python multi_user_sync.py list_users")
        sys.exit(1)
    
    command = sys.argv[1]
    
    if command == "add_user":
        if len(sys.argv) < 4:
            print("Usage: python multi_user_sync.py add_user <email> <user_api_id> [calendar_id]")
            sys.exit(1)
        
        email = sys.argv[2]
        user_api_id = sys.argv[3]
        calendar_id = sys.argv[4] if len(sys.argv) > 4 else 'primary'
        
        if sync.add_user(email, user_api_id, calendar_id):
            print(f"Utilisateur {email} ajouté avec succès")
        else:
            print(f"Erreur lors de l'ajout de l'utilisateur {email}")
            sys.exit(1)
    
    elif command == "sync":
        if not sync.sync_all_users():
            sys.exit(1)
    
    elif command == "list_users":
        users = sync.get_active_users()
        if users:
            print("Utilisateurs actifs:")
            for user in users:
                print(f"  - {user['email']} (API ID: {user['user_api_id']}, "
                      f"Dernière sync: {user['last_sync'] or 'Jamais'})")
        else:
            print("Aucun utilisateur actif")
    
    else:
        print(f"Commande inconnue: {command}")
        sys.exit(1)

if __name__ == '__main__':
    main()