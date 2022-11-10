
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
```

## Nouveautés
- Couleurs dans les réponses
- Autocomplétion du nom des joueurs
- Erreur quand la commande est exécutée dans la console ou dans un command block
- Envoi des joueurs qui se connectent au lobby
Et pour finir le plus important : Plus de bug qui utilise beaucoup de CPU pour rien !
