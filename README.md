
# Plugin Bungeecord pour la commande /lobby

Ajoute la commande /looby

## Description de la commande

`/lobby` (alias : `/hub`) :
- Sans arguments : téléporte le joueur ayant effectué la commande au lobby
- Avec 1 argument `<player>`: téléporte le joueur `<player>` au lobby, nécessite le permission `lobby.other`

## Lobby
La commande détecte les serveurs avec "hub" ou "lobby" dans leur nom, et téléporrte au lobby le plus peuplé ayant moins de `min_player` joueurs, si aucun lobby ne rempli ces conditions, téléporte au lobby le moins peuplé.

## Min_player
Pour configurer `min_player`, changez le paramètre `min_player` dans le fichier `config.yml`
#### Exemple de fichier `config.yml`:
```
min_player: 6
délai_update_lobby: 5
```

## Nouveautés 2.3
- Plus d'envoi des joueurs au lobby lors de la connection
