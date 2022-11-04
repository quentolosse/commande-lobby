
# Plugin Bungeecord pour la commande /lobby

Ajoute la commande /looby

## Description de la commande

`/lobby` (alias : `/hub`) :
- Sans arguments : téléporte le joueur ayant effectué la commande au lobby
- Avec 1 argument `<player>`: téléporte le joueur `<player>` au lobby, nécessite le permission `lobby.other`

## Lobby
La commande détecte les serveurs avec "hub" ou "lobby" dans leur nom, et téléporrte au lobby le moins peuplé ayant au minimun `min_player` joueurs, si aucun lobby ne rempli ces conditions, téléporte au lobby le plus peuplé.

## Min_player
Pour configurer `min_player`, changez le paramètre `min_player` dans le fichier `config.yml`
#### Exemple de fichier `config.yml`:
```
min_player: 6
```
