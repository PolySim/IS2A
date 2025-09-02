#!/usr/bin/env python3
"""
Script de synchronisation d'événements API vers Google Calendar
Compatible avec l'exécution via CRON
"""

import os
import json
import pickle
import requests
from datetime import datetime, timedelta
from google.auth.transport.requests import Request
from google_auth_oauthlib.flow import InstalledAppFlow
from googleapiclient.discovery import build
from googleapiclient.errors import HttpError
import hashlib
import logging
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
        logging.FileHandler('calendar_sync.log'),
        logging.StreamHandler()
    ]
)
logger = logging.getLogger(__name__)

class GoogleCalendarSync:
    def __init__(self, calendar_id='primary'):
        self.calendar_id = calendar_id
        self.service = None
        self.scopes = ['https://www.googleapis.com/auth/calendar']
        
        # Configuration - À adapter selon votre API
        self.api_url = os.getenv('API_URL', 'https://votre-api.com/events')
        self.api_headers = {
            'Content-Type': 'application/json'
        }
        
        # Fichier pour stocker l'état des événements
        self.state_file = 'calendar_sync_state.json'
        
    def authenticate_google(self):
        """Authentification avec l'API Google Calendar"""
        creds = None
        
        # Chargement des credentials existants
        if os.path.exists('token.pickle'):
            with open('token.pickle', 'rb') as token:
                creds = pickle.load(token)
        
        # Si pas de credentials valides, demander l'authentification
        if not creds or not creds.valid:
            if creds and creds.expired and creds.refresh_token:
                creds.refresh(Request())
            else:
                if not os.path.exists('credentials.json'):
                    logger.error("Fichier credentials.json manquant. Téléchargez-le depuis Google Cloud Console.")
                    return False
                    
                flow = InstalledAppFlow.from_client_secrets_file(
                    'credentials.json', self.scopes)
                creds = flow.run_local_server(port=0)
            
            # Sauvegarde des credentials
            with open('token.pickle', 'wb') as token:
                pickle.dump(creds, token)
        
        self.service = build('calendar', 'v3', credentials=creds)
        return True
    
    def fetch_api_events(self):
        """Récupération des événements depuis l'API externe"""
        try:
            today = datetime.now()
            start_date = today.strftime('%Y-%m-%d')
            end_date = (today + timedelta(days=30)).strftime('%Y-%m-%d')
            
            url = f"{self.api_url}&start={start_date}&end={end_date}"
            response = requests.get(url, headers=self.api_headers, timeout=30)
            response.raise_for_status()
            
            events = response.json()
            logger.info(f"Récupéré {len(events)} événements depuis l'API pour la période {start_date} à {end_date}")
            return events
            
        except requests.RequestException as e:
            logger.error(f"Erreur lors de la récupération des événements : {e}")
            return []
    
    def normalize_event(self, api_event):
        """
        Normalise un événement de l'API vers le format Google Calendar
        Structure API : title, description, start (format: 20250901T101500Z), end
        Les dates de l'API sont en UTC et doivent être converties à l'heure française
        """
        try:
            # Définition des fuseaux horaires
            utc = pytz.UTC
            paris_tz = pytz.timezone('Europe/Paris')
            
            # Conversion du format datetime de l'API (20250901T101500Z) depuis UTC
            start_dt_utc = datetime.strptime(api_event['start'], '%Y%m%dT%H%M%SZ')
            end_dt_utc = datetime.strptime(api_event['end'], '%Y%m%dT%H%M%SZ')
            
            # Assignation du fuseau UTC aux objets datetime
            start_dt_utc = utc.localize(start_dt_utc)
            end_dt_utc = utc.localize(end_dt_utc)
            
            # Conversion vers l'heure française (Europe/Paris)
            start_dt_paris = start_dt_utc.astimezone(paris_tz)
            end_dt_paris = end_dt_utc.astimezone(paris_tz)
            
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
            
            # Génération d'un hash unique basé sur le contenu de l'événement
            event_content = f"{api_event.get('title', '')}{api_event.get('start', '')}{api_event.get('end', '')}"
            event_hash = hashlib.md5(event_content.encode()).hexdigest()
            
            # Ajout de l'identifiant dans la description pour le tracking
            event['description'] += f"\n\n[API_SYNC_ID: {event_hash}]"
            
            return event, event_hash
            
        except Exception as e:
            logger.error(f"Erreur lors de la normalisation de l'événement {api_event}: {e}")
            return None, None
    
    def load_state(self):
        """Chargement de l'état précédent"""
        if os.path.exists(self.state_file):
            try:
                with open(self.state_file, 'r') as f:
                    return json.load(f)
            except Exception as e:
                logger.warning(f"Erreur lors du chargement de l'état : {e}")
        return {}
    
    def save_state(self, state):
        """Sauvegarde de l'état actuel"""
        try:
            with open(self.state_file, 'w') as f:
                json.dump(state, f, indent=2)
        except Exception as e:
            logger.error(f"Erreur lors de la sauvegarde de l'état : {e}")
    
    def find_existing_event(self, event_hash):
        """Recherche un événement existant par son hash"""
        try:
            # Recherche dans les 30 prochains jours
            time_min = datetime.utcnow().isoformat() + 'Z'
            time_max = (datetime.utcnow() + timedelta(days=30)).isoformat() + 'Z'
            
            events_result = self.service.events().list(
                calendarId=self.calendar_id,
                timeMin=time_min,
                timeMax=time_max,
                q=f"[API_SYNC_ID: {event_hash}]",
                singleEvents=True,
                orderBy='startTime'
            ).execute()
            
            events = events_result.get('items', [])
            return events[0] if events else None
            
        except HttpError as e:
            logger.error(f"Erreur lors de la recherche d'événement : {e}")
            return None
    
    def create_event(self, event):
        """Création d'un nouvel événement"""
        try:
            created_event = self.service.events().insert(
                calendarId=self.calendar_id,
                body=event
            ).execute()
            
            logger.info(f"Événement créé : {event['summary']}")
            return created_event
            
        except HttpError as e:
            logger.error(f"Erreur lors de la création de l'événement : {e}")
            return None
    
    def update_event(self, event_id, event):
        """Mise à jour d'un événement existant"""
        try:
            updated_event = self.service.events().update(
                calendarId=self.calendar_id,
                eventId=event_id,
                body=event
            ).execute()
            
            logger.info(f"Événement mis à jour : {event['summary']}")
            return updated_event
            
        except HttpError as e:
            logger.error(f"Erreur lors de la mise à jour de l'événement : {e}")
            return None
    
    def delete_event(self, event_id):
        """Suppression d'un événement"""
        try:
            self.service.events().delete(
                calendarId=self.calendar_id,
                eventId=event_id
            ).execute()
            
            logger.info(f"Événement supprimé : {event_id}")
            return True
            
        except HttpError as e:
            logger.error(f"Erreur lors de la suppression de l'événement : {e}")
            return False
    
    def sync_events(self):
        """Synchronisation complète des événements"""
        if not self.authenticate_google():
            logger.error("Échec de l'authentification Google")
            return False
        
        # Récupération des événements de l'API
        api_events = self.fetch_api_events()
        if not api_events:
            logger.warning("Aucun événement récupéré depuis l'API")
            return False
        
        # Chargement de l'état précédent
        previous_state = self.load_state()
        current_state = {}
        
        stats = {'created': 0, 'updated': 0, 'deleted': 0, 'errors': 0}
        
        # Traitement des événements de l'API
        for api_event in api_events:
            event, event_hash = self.normalize_event(api_event)
            if not event or not event_hash:
                stats['errors'] += 1
                continue
            
            current_state[event_hash] = {
                'summary': event['summary'],
                'last_sync': datetime.utcnow().isoformat()
            }
            
            # Vérification si l'événement existe déjà
            existing_event = self.find_existing_event(event_hash)
            
            if existing_event:
                # Mise à jour de l'événement existant
                if self.update_event(existing_event['id'], event):
                    stats['updated'] += 1
                    current_state[event_hash]['google_id'] = existing_event['id']
                else:
                    stats['errors'] += 1
            else:
                # Création d'un nouvel événement
                created_event = self.create_event(event)
                if created_event:
                    stats['created'] += 1
                    current_state[event_hash]['google_id'] = created_event['id']
                else:
                    stats['errors'] += 1
        
        # Suppression des événements qui ne sont plus dans l'API
        events_to_delete = set(previous_state.keys()) - set(current_state.keys())
        for event_hash in events_to_delete:
            if 'google_id' in previous_state[event_hash]:
                if self.delete_event(previous_state[event_hash]['google_id']):
                    stats['deleted'] += 1
                else:
                    stats['errors'] += 1
        
        # Sauvegarde du nouvel état
        self.save_state(current_state)
        
        # Rapport de synchronisation
        logger.info(f"Synchronisation terminée - Créés: {stats['created']}, "
                   f"Mis à jour: {stats['updated']}, Supprimés: {stats['deleted']}, "
                   f"Erreurs: {stats['errors']}")
        
        return True

def main():
    """Fonction principale"""
    try:
        # Initialisation du synchroniseur
        sync = GoogleCalendarSync()
        
        # Lancement de la synchronisation
        success = sync.sync_events()
        
        if success:
            logger.info("Synchronisation réussie")
        else:
            logger.error("Échec de la synchronisation")
            exit(1)
            
    except Exception as e:
        logger.error(f"Erreur critique : {e}")
        exit(1)

if __name__ == '__main__':
    main()