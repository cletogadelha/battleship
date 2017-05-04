package com.cletogadelha.controller;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cletogadelha.domain.BoardPlacement;
import com.cletogadelha.domain.Game;
import com.cletogadelha.service.GameService;

@RestController
@RequestMapping("/rest/game")
public class GameController extends CRUDController<Game> {
	
	@RequestMapping(value="create/{player_1}", method=RequestMethod.POST)
	public ResponseEntity<?> join(@PathVariable("player_1") UUID player1) {
		return ResponseEntity.ok(((GameService) getService()).createNewGame(player1)); 
	}
	
	@RequestMapping(value="{game_id}/join/{player_2}", method=RequestMethod.POST)
	public ResponseEntity<?> join(@PathVariable("game_id") UUID gameId, @PathVariable("player_2") UUID player2) {
		return ResponseEntity.ok(((GameService) getService()).joinGame(gameId, player2)); 
	}
	
	@RequestMapping(value="{game_id}/player/{playerId}/setup", method=RequestMethod.POST)
	public ResponseEntity<?> setupShip(@PathVariable("game_id") UUID gameId, 
			@PathVariable("playerId") UUID playerId, @RequestBody @Valid BoardPlacement boardPlacement) {
		return ResponseEntity.ok(((GameService) getService()).setupShip(gameId, playerId, boardPlacement)); 
	}
	
}
