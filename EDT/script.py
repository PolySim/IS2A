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
            end_date = (today + timedelta(days=2)).strftime('%Y-%m-%d')
            
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
        Les dates de l'API sont déjà en heure française locale
        """
        try:
            # Définition du fuseau horaire français
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
        """
        Synchronisation optimisée des événements
        1. Nettoie les anciens événements (supprime ceux qui ne sont plus dans l'API ou qui ont changé)
        2. Ajoute les nouveaux événements (seulement ceux qui n'existaient pas ou qui ont changé)
        """
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
        
        # Normalisation des événements de l'API pour créer un dictionnaire de référence
        api_events_dict = {}
        for api_event in api_events:
            event, event_hash = self.normalize_event(api_event)
            if event and event_hash:
                api_events_dict[event_hash] = {
                    'event': event,
                    'summary': event['summary'],
                    'last_sync': datetime.utcnow().isoformat()
                }
        
        stats = {'created': 0, 'updated': 0, 'deleted': 0, 'unchanged': 0, 'errors': 0}
        
        # ÉTAPE 1: Nettoyer les anciens événements
        logger.info("Étape 1: Nettoyage des anciens événements...")
        for old_hash, old_data in previous_state.items():
            if old_hash not in api_events_dict:
                # L'événement n'est plus dans l'API -> le supprimer
                if 'google_id' in old_data:
                    if self.delete_event(old_data['google_id']):
                        stats['deleted'] += 1
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
                    if 'google_id' in old_data:
                        if self.delete_event(old_data['google_id']):
                            stats['updated'] += 1  # Compte comme une mise à jour
                            logger.info(f"Modifié (suppression): {old_summary} -> {api_summary}")
                        else:
                            stats['errors'] += 1
                else:
                    # L'événement n'a pas changé -> le conserver
                    stats['unchanged'] += 1
                    # Conserver l'ID Google pour éviter de le recréer
                    api_events_dict[old_hash]['google_id'] = old_data.get('google_id')
        
        # ÉTAPE 2: Ajouter les nouveaux événements
        logger.info("Étape 2: Ajout des nouveaux événements...")
        current_state = {}
        for event_hash, event_data in api_events_dict.items():
            current_state[event_hash] = {
                'summary': event_data['summary'],
                'last_sync': event_data['last_sync']
            }
            
            if 'google_id' in event_data:
                # L'événement existe déjà et n'a pas changé -> ne rien faire
                current_state[event_hash]['google_id'] = event_data['google_id']
                continue
            
            # L'événement est nouveau ou a changé -> le créer
            created_event = self.create_event(event_data['event'])
            if created_event:
                if event_hash in previous_state:
                    # C'était une modification (déjà compté dans updated)
                    logger.info(f"Modifié (création): {event_data['summary']}")
                else:
                    # C'est un nouvel événement
                    stats['created'] += 1
                    logger.info(f"Créé: {event_data['summary']}")
                current_state[event_hash]['google_id'] = created_event['id']
            else:
                stats['errors'] += 1
        
        # Sauvegarde du nouvel état
        self.save_state(current_state)
        
        # Rapport de synchronisation
        logger.info(f"Synchronisation terminée - Créés: {stats['created']}, "
                   f"Mis à jour: {stats['updated']}, Supprimés: {stats['deleted']}, "
                   f"Inchangés: {stats['unchanged']}, Erreurs: {stats['errors']}")
        
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