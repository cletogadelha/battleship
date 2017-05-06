# battleship

Initial implementation of an Battleship API using Spring Boot 1.5.x and PostgresSQL

## API Endpoints

Basic endpoints to start to play

* `POST` `rest/player` creates and returns a player
* `POST` `rest/game/create/{player_id}` returns a new game for the player
* `POST` `rest/game/{game_id}/join/{player_id}` join an existing game
* `POST` `rest/game/{game_id}/player/{playerId}/setup` setup a ship on the board.
* `POST` `rest/game/{game_id}/player/{playerId}/move` make a move.
* `POST` `/rest/game/{game_id}/flipCoin` flipACoin which decides who starts the game when every ship has already been deployed
* `GET` `rest/game/{game_id}/status` get the all the data of the game
* `POST` `rest/game/{game_id}/pause` pause a game that is in progress.
* `POST` `{game_id}/resume` resume a paused game.
* `GET` `/leaderboard` shows the ranking
