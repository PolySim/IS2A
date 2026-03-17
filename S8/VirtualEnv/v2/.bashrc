# ~/.bashrc - modèle de base pour étudiants info / stats / data
# ------------------------------------------------------------
# Objectif : un shell confortable, lisible, sans magie cachée.

# Ne rien faire si le shell n'est pas interactif
[[ $- != *i* ]] && return

# =========================
# Historique des commandes
# =========================

# Taille de l'historique
HISTSIZE=5000
HISTFILESIZE=20000

# Ne pas enregistrer les doublons successifs, ni les commandes qui commencent par un espace
HISTCONTROL=ignoreboth

# Timestamp dans l'historique (avec 'history')
HISTTIMEFORMAT='%F %T '

# Meilleure gestion de l'historique
shopt -s histappend   # on ajoute au fichier, on ne l'écrase pas
shopt -s cmdhist      # commandes multi-lignes sur une seule entrée

# Partage de l'historique entre terminaux
PROMPT_COMMAND='history -a; history -n'

# =========================
# Options de confort Bash
# =========================

# Corrige les petites fautes dans 'cd'
shopt -s cdspell

# Permet de faire "nom_de_dossier" au lieu de "cd nom_de_dossier"
shopt -s autocd

# ** récursif : ls **/*.py
shopt -s globstar

# Adapte la taille du terminal pour les programmes comme 'less'
shopt -s checkwinsize

# Sécurité : éviter d'écraser des fichiers avec '>'
set -o noclobber

# Edition en mode Emacs (par défaut) ou décommente pour Vim :
# set -o vi

# =========================
# Alias utiles
# =========================

# ls amélioré
if command -v dircolors >/dev/null 2>&1; then
    eval "$(dircolors -b)"
fi
alias ls='ls --color=auto'
alias ll='ls -lah'
alias la='ls -A'
alias l='ls -CF'

# Navigation
alias ..='cd ..'
alias ...='cd ../..'

# Sécurité fichiers
alias rm='rm -i'
alias cp='cp -i'
alias mv='mv -i'

# Historique
alias h='history'

# Nettoyage écran
alias c='clear'

# =========================
# Raccourcis clavier (readline)
# =========================

# Tab → complétion "menu" (cyclique)
bind '"\t": menu-complete'

# Ctrl+R → recherche dans l'historique
bind '"\C-r": reverse-search-history'

# Flèches ↑↓ → recherchent dans l'historique à partir du début tapé
bind '"\e[A": history-search-backward'
bind '"\e[B": history-search-forward'

# Home / End fonctionnels sur la plupart des terminaux
bind '"\e[1~": beginning-of-line'
bind '"\e[4~": end-of-line'
bind '"\e[H": beginning-of-line'
bind '"\e[F": end-of-line'

# =========================
# PATH et éditeur
# =========================

# Pour se connecter sur le serveur postgres
export PGHOST=serveur-etu.polytech-lille.fr

# Editeur par défaut (changer en vim si tu préfères)
export EDITOR=nano
export VISUAL=$EDITOR

# =========================
# Python / R (profil étudiant data)
# =========================

# Utiliser python3 par défaut si disponible
if command -v python3 >/dev/null 2>&1; then
    alias python='python3'
    alias pip='python3 -m pip'
fi

# R : dossier de librairies perso
export R_LIBS_USER="$HOME/R/libs"
[ -d "$R_LIBS_USER" ] || mkdir -p "$R_LIBS_USER"

# =========================
# Infos Git dans le prompt
# =========================

# Charge git-prompt si disponible
if [ -f /usr/share/git/completion/git-prompt.sh ]; then
    source /usr/share/git/completion/git-prompt.sh
elif [ -f /etc/bash_completion.d/git-prompt ]; then
    source /etc/bash_completion.d/git-prompt
fi

# Si __git_ps1 n'existe pas, en définir une vide pour éviter les erreurs
type __git_ps1 >/dev/null 2>&1 || __git_ps1() { :; }

# Options d'affichage git
export GIT_PS1_SHOWDIRTYSTATE=1        # +* indique les modifs
export GIT_PS1_SHOWUNTRACKEDFILES=1    # % fichiers non suivis
export GIT_PS1_SHOWSTASHSTATE=1        # $ stash présent
export GIT_PS1_SHOWUPSTREAM="auto"     # ↑↓ ahead / behind

# =========================
# Prompt (PS1) lisible et informatif
# =========================

# Prompt sur une ligne :
# [HH:MM:SS] user@machine chemin [branche_git] $
PS1='\n'                                                # ligne vide pour aérer
PS1+='\[\e[1;30m\][\t]\[\e[0m\] '                       # heure en gris
PS1+='\[\e[1;32m\]\u@\h\[\e[0m\] '                      # user@host en vert
PS1+='\[\e[1;34m\]\w\[\e[0m\]'                          # chemin en bleu
PS1+='\[\e[1;33m\]$(__git_ps1 " [%s]")\[\e[0m\]'        # [branche git] en jaune
PS1+=' \$ '                                            # $ sur la 2e ligne

export PS1

# =========================
# Commande Perso
# =========================

# Utilisation notamment de poetry
export PYTHON_KEYRING_BACKEND=keyring.backends.null.Keyring
