package com.cletogadelha.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cletogadelha.domain.BoardPlacement;
import com.cletogadelha.domain.Game;
import com.cletogadelha.domain.Move;
import com.cletogadelha.exception.BattleshipException;
import com.cletogadelha.service.GameService;

@RestController
@RequestMapping("/rest/game")
public class GameController<T> extends CRUDController<Game> {
	
	@RequestMapping(value="create/{player_1}", method=RequestMethod.POST)
	public ResponseEntity<Game> create(@PathVariable("player_1") Integer player1) throws BattleshipException {
		return new ResponseEntity<Game>(((GameService) getService()).createNewGame(player1), HttpStatus.CREATED);
	}
	
	@RequestMapping(value="{game_id}/join/{player_2}", method=RequestMethod.POST)
	public ResponseEntity<Game> join(@PathVariable("game_id") Integer gameId, 
			@PathVariable("player_2") Integer player2) throws BattleshipException {
		return ResponseEntity.ok(((GameService) getService()).joinGame(gameId, player2)); 
	}
	
	@RequestMapping(value="{game_id}/player/{playerId}/setup", method=RequestMethod.POST)
	public ResponseEntity<Game> setupShip(@PathVariable("game_id") Integer gameId, 
			@PathVariable("playerId") Integer playerId, @RequestBody @Valid BoardPlacement boardPlacement) throws BattleshipException {
		return ResponseEntity.ok(((GameService) getService()).setupShip(gameId, playerId, boardPlacement)); 
	}
	
	@RequestMapping(value="{game_id}/player/{playerId}/move", method=RequestMethod.POST)
	public ResponseEntity<Game> makeAMove(@PathVariable("game_id") Integer gameId, 
			@PathVariable("playerId") Integer playerId, @RequestBody @Valid Move move) throws BattleshipException {
		return ResponseEntity.ok(((GameService) getService()).makeAMove(gameId, playerId, move));
	}
	
	@RequestMapping(value="{game_id}/coinFlip", method=RequestMethod.POST)
	public ResponseEntity<Game> coinFlip(@PathVariable("game_id") Integer gameId) throws BattleshipException {
		return ResponseEntity.ok(((GameService) getService()).coinFlip(gameId));
	}
	
	@RequestMapping(value="{game_id}/status", method=RequestMethod.GET)
	public ResponseEntity<Game> gameStatus(@PathVariable("game_id") Integer gameId) throws BattleshipException {
		return ResponseEntity.ok(((GameService) getService()).gameStatus(gameId));
	}
	
	@Override
	public ResponseEntity<Game> create(@RequestBody @Valid Game type) throws BattleshipException{
		throw new BattleshipException("You cannot create a game without a player");
	}
	
}
