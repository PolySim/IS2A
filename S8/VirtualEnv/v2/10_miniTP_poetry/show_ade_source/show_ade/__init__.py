# __init__.py
from __future__ import annotations

import locale
import urllib.parse
from datetime import datetime
from typing import Optional

import requests
from ics import Calendar
from rich.console import Console
from rich.table import Table
from zoneinfo import ZoneInfo

__all__ = [
    "build_ics_url",
    "fetch_calendar",
    "render_table",
    "render_table_text",
    "get_planning_text",
    "main",
]

BASE_URL = "https://planning-gestionnaire-2025.univ-lille.fr/jsp/custom/modules/plannings/anonymous_cal.jsp"
DEFAULT_TZ = "Europe/Paris"
DEFAULT_FIRST_DATE = "2025-05-18"
DEFAULT_LAST_DATE = "2026-11-29"


def _set_french_locale() -> None:
    """Essaie de mettre la locale française pour les noms de jours/mois."""
    try:
        locale.setlocale(locale.LC_TIME, "fr_FR.UTF-8")
    except locale.Error:
        # Laisse la locale système si fr_FR n’est pas dispo
        locale.setlocale(locale.LC_TIME, "")


def build_ics_url(
    code: str,
    first_date: str = DEFAULT_FIRST_DATE,
    last_date: str = DEFAULT_LAST_DATE,
    display_config_id: str = "2",
) -> str:
    """Construit l’URL du fichier .ics sans faire l’appel réseau."""
    params = {
        "codeY": code,
        "projectId": "0",
        "calType": "ical",
        "firstDate": first_date,
        "lastDate": last_date,
        "displayConfigId": display_config_id,
    }
    return f"{BASE_URL}?{urllib.parse.urlencode(params)}"


def fetch_calendar(
    code: str,
    first_date: str = DEFAULT_FIRST_DATE,
    last_date: str = DEFAULT_LAST_DATE,
    display_config_id: str = "2",
    timeout: float = 20.0,
) -> Calendar:
    """Télécharge et parse le calendrier ICS."""
    url = build_ics_url(code, first_date, last_date, display_config_id)
    r = requests.get(url, timeout=timeout)
    r.raise_for_status()
    return Calendar(r.text)


def _parse_now(now_str: Optional[str], tz: ZoneInfo) -> datetime:
    """Interprète une date 'YYYY-MM-DD HH:MM' ou prend l’instant présent."""
    if now_str:
        try:
            dt = datetime.strptime(now_str, "%Y-%m-%d %H:%M")
            return dt.replace(tzinfo=tz)
        except ValueError as e:
            raise ValueError("Format invalide pour 'now' (attendu 'YYYY-MM-DD HH:MM').") from e
    return datetime.now(tz)


def render_table(
    cal: Calendar,
    now: datetime,
    tz: ZoneInfo,
) -> Table:
    """Construit la table Rich à partir d’un Calendar ics."""
    # locale pour %A, %b en français
    _set_french_locale()

    events = sorted(cal.events, key=lambda e: e.begin)
    current_week = None
    next_marker_shown = False

    table = Table(show_lines=True)
    table.add_column("Semaine", style="bold", no_wrap=True, justify="center")
    table.add_column("Date", style="cyan", justify="center")
    table.add_column("Heure", style="magenta", no_wrap=True, justify="center")
    table.add_column("Événement", style="green", justify="center")
    table.add_column("Num", style="white", no_wrap=True, justify="center")
    table.add_column("Salle", style="blue", justify="center")

    compteur_evenements: dict[str, int] = {}

    for event in events:
        begin_local = event.begin.astimezone(tz)
        end_local = event.end.astimezone(tz)
        week = begin_local.isocalendar().week
        year = begin_local.year

        if current_week != (year, week):
            if current_week is not None:
                # ligne de séparation entre semaines (6 colonnes)
                table.add_row(*(["─" * 5] * 6))
            current_week = (year, week)
            semaine_label = f"{week:02d}-{year}"
        else:
            semaine_label = ""

        date_str = begin_local.strftime("%A %d %b %Y")
        heure_str = f"{begin_local.strftime('%H:%M')} → {end_local.strftime('%H:%M')}"
        nom_event = event.name or "(Sans titre)"
        rang = compteur_evenements.get(nom_event, 0) + 1
        compteur_evenements[nom_event] = rang

        salle = (event.location or "—").replace(",", "\n")

        # Marqueur avant le premier événement à venir
        if not next_marker_shown and begin_local > now:
            table.add_row(
                "",
                "[yellow bold]👉 PROCHAIN COURS[/yellow bold]",
                "[yellow bold]↓↓↓[/yellow bold]",
                "[yellow bold]↓↓↓[/yellow bold]",
                "[yellow bold]↓↓↓[/yellow bold]",
                ""
            )
            next_marker_shown = True

        table.add_row(
            semaine_label,
            date_str,
            heure_str,
            nom_event,
            str(rang),
            salle,
        )

    return table


def render_table_text(table: Table) -> str:
    """Rend la table Rich en texte simple (utile pour logs/retours)."""
    console = Console(record=True)
    console.print(table)
    return console.export_text()


def get_planning_text(
    code: str,
    now_str: Optional[str] = None,
    tz_name: str = DEFAULT_TZ,
    url_only: bool = False,
    first_date: str = DEFAULT_FIRST_DATE,
    last_date: str = DEFAULT_LAST_DATE,
    display_config_id: str = "2",
) -> str:
    """
    API haut-niveau : renvoie soit l’URL ICS, soit la table rendue en texte.
    """
    tz = ZoneInfo(tz_name)

    if url_only:
        print(build_ics_url(code, first_date, last_date, display_config_id))
        return build_ics_url(code, first_date, last_date, display_config_id)

    cal = fetch_calendar(code, first_date, last_date, display_config_id)
    now = _parse_now(now_str, tz)
    table = render_table(cal, now, tz)
    return render_table_text(table)


def main(argv: Optional[list[str]] = None) -> int:
    """
    Petite entrée CLI facultative. À appeler depuis un __main__.py.
    - argv est une liste de strings (sans le binaire), ou None pour sys.argv[1:].
    """
    import argparse

    parser = argparse.ArgumentParser(description="Affiche un planning depuis un email universitaire.")
    parser.add_argument("code", help="Code Y, ex: 4YMHMH-120")
    parser.add_argument("--now", type=str, help="Date/heure simulées 'YYYY-MM-DD HH:MM' (défaut: maintenant).")
    parser.add_argument("--url-only", action="store_true", help="Afficher uniquement l’URL du .ics puis quitter.")
    parser.add_argument("--tz", default=DEFAULT_TZ, help=f"Fuseau horaire (défaut: {DEFAULT_TZ}).")
    args = parser.parse_args(argv)

    try:
        out = get_planning_text(
            code=args.code,
            now_str=args.now,
            tz_name=args.tz,
            url_only=args.url_only,
        )
        # print(out)
        return 0
    except Exception as e:
        Console().print(f"[red]❌ {e}[/red]")
        return 1
