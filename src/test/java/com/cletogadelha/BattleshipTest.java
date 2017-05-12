package com.cletogadelha;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;

import com.cletogadelha.domain.BoardPlacement;
import com.cletogadelha.domain.Coordinate;
import com.cletogadelha.domain.Game;
import com.cletogadelha.domain.Move;
import com.cletogadelha.domain.MoveResponse;
import com.cletogadelha.domain.enums.Direction;
import com.cletogadelha.domain.enums.GameStatus;
import com.cletogadelha.domain.enums.MoveStatus;
import com.cletogadelha.domain.enums.Ships;
import com.cletogadelha.exception.BattleshipErrorResponse;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@SqlGroup({
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts="classpath:game-data.sql"),
	@Sql(executionPhase = ExecutionPhase.AFTER_TEST_METHOD, scripts="classpath:drop-game.sql") 
})
public class BattleshipTest {
	
	private static final String URL = "/rest/game/";

	@Autowired
	private TestRestTemplate restTemplate;
	
	@Test
	public void shouldCreateNewGame(){
		ResponseEntity<Game> response = 
			restTemplate.postForEntity(URL + "create/1", null, Game.class);
		
		assertEquals(response.getStatusCode(), HttpStatus.CREATED);
		assertNotNull(response.getBody().getId());
		assertEquals(response.getBody().getGameStatus(), GameStatus.WAITING_OPPONENT);
	}
	
	@Test
	public void shouldNotCreateNewGameWhenPlayerIsInvalid(){
		ResponseEntity<BattleshipErrorResponse> response = 
			restTemplate.postForEntity(URL + "create/4", null, BattleshipErrorResponse.class);
		assertEquals(response.getBody().getMessage(), "Player not found!");
		assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
	}
	
	@Test
	public void shouldJoinAGame(){
		ResponseEntity<Game> response = 
			restTemplate.postForEntity(URL + "1/join/2", null, Game.class);
		
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		assertEquals(response.getBody().getGameStatus(), GameStatus.SETUP_PHASE);
	}
	
	@Test
	public void shouldNotJoinAGameWhenGameIsInvalid(){
		ResponseEntity<BattleshipErrorResponse> response =
			restTemplate.postForEntity(URL + "6/join/1", null, BattleshipErrorResponse.class);
		
		assertEquals(response.getBody().getMessage(), "Game not found!");
		assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
	}
	
	@Test
	public void shouldNotJoinAGameWhenPlayerIsInvalid(){
		ResponseEntity<BattleshipErrorResponse> response =
			restTemplate.postForEntity(URL + "1/join/4", null, BattleshipErrorResponse.class);
		
		assertEquals(response.getBody().getMessage(), "Player not found!");
		assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
	}
	
	@Test
	public void shouldNotJoinAGameWhenGameIsNotWaitingPlayers(){
		ResponseEntity<BattleshipErrorResponse> response =
			restTemplate.postForEntity(URL + "2/join/2", null, BattleshipErrorResponse.class);
		
		assertEquals(response.getBody().getMessage(), "Sorry! This game is not available to enter!");
		assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void shouldNotJoinAGameWhenPlayerIsAlreadyInTheGame(){
		ResponseEntity<BattleshipErrorResponse> response =
			restTemplate.postForEntity(URL + "1/join/1", null, BattleshipErrorResponse.class);
		
		assertEquals(response.getBody().getMessage(), "You are already in this game!");
		assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void shouldSetupAShip(){
		BoardPlacement boardPlacement = getMockBoardPlacement();
		
		ResponseEntity<Game> response = 
				restTemplate.postForEntity(URL + "2/player/1/setup", boardPlacement, Game.class);
		
		assertEquals(response.getStatusCode(), HttpStatus.OK);
	}
	
	@Test
	public void shouldNotSetupAShipWhenGameIsntReadyToSetup(){
		BoardPlacement boardPlacement = getMockBoardPlacement();
		
		ResponseEntity<BattleshipErrorResponse> response =
			restTemplate.postForEntity(URL + "1/player/1/setup", boardPlacement, BattleshipErrorResponse.class);
		
		assertEquals(response.getBody().getMessage(), "Sorry! You cannot setup ships in this game!");
		assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void shouldNotSetupAShipWhenPositionIsOutOfBoardRange(){
		BoardPlacement boardPlacement = getMockBoardPlacement();
		boardPlacement.setInitialCoordinate(new Coordinate("J", 8));
		boardPlacement.setDirection(Direction.VERTICAL);
		
		ResponseEntity<BattleshipErrorResponse> response =
			restTemplate.postForEntity(URL + "2/player/1/setup", boardPlacement, BattleshipErrorResponse.class);
		
		assertEquals(response.getBody().getMessage(), "You cannot place a ship on that position!");
		assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void shouldNotSetupAShipWhenPositionIsAlreadyTaken(){
		BoardPlacement boardPlacement = getMockBoardPlacement();
		boardPlacement.setInitialCoordinate(new Coordinate("A", 1));
		
		ResponseEntity<BattleshipErrorResponse> response =
			restTemplate.postForEntity(URL + "2/player/1/setup", boardPlacement, BattleshipErrorResponse.class);
		
		assertEquals(response.getBody().getMessage(), "Oops! There is a ship deployed in here!");
		assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void shouldNotSetupAShipWhenShipIsAlreadyPlaced(){
		BoardPlacement boardPlacement = getMockBoardPlacement();
		boardPlacement.setShip(Ships.SUBMARINE);
		
		ResponseEntity<BattleshipErrorResponse> response =
			restTemplate.postForEntity(URL + "2/player/1/setup", boardPlacement, BattleshipErrorResponse.class);
		
		assertEquals(response.getBody().getMessage(), "You have already deployed this kind of ship!");
		assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void shoulFlipCoin(){
		ResponseEntity<Game> response = 
				restTemplate.postForEntity(URL + "3/flipCoin", null, Game.class);
		
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		assertNotNull(response.getBody().getTurnHolder());
	}
	
	@Test
	public void shoulNotFlipCoin(){
		ResponseEntity<BattleshipErrorResponse> response = 
				restTemplate.postForEntity(URL + "1/flipCoin", null, BattleshipErrorResponse.class);
		
		assertEquals(response.getBody().getMessage(), "The game is not at flip coin phase! Let's wait until the players finish the deployment");
		assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void shouldMakeAMoveAndMiss(){
		Move move = getMockMove();
		move.setCoordinate(new Coordinate("B", 4));
		
		ResponseEntity<MoveResponse> response = 
				restTemplate.postForEntity(URL + "4/player/1/move", move, MoveResponse.class);
		
		assertEquals(response.getBody().getStatus(), MoveStatus.MISSED);
		assertEquals(response.getStatusCode(), HttpStatus.OK);
	}
	
	@Test
	public void shouldMakeAMoveAndHit(){
		Move move = getMockMove();
		
		ResponseEntity<MoveResponse> response = 
				restTemplate.postForEntity(URL + "4/player/1/move", move, MoveResponse.class);
		
		assertEquals(response.getBody().getStatus(), MoveStatus.HIT);
		assertEquals(response.getStatusCode(), HttpStatus.OK);
	}
	
	@Test
	public void shouldMakeAMoveAndWin(){
		Move move = getMockMove();
		move.setCoordinate(new Coordinate("A",2));
		
		ResponseEntity<MoveResponse> response = 
				restTemplate.postForEntity(URL + "100/player/1/move", move, MoveResponse.class);
		
		assertEquals(response.getBody().getStatus(), MoveStatus.WON);
		assertEquals(response.getBody().getGame().getGameStatus(), GameStatus.FINISHED);
		assertEquals(response.getStatusCode(), HttpStatus.OK);
	}
	
	@Test
	public void shouldNotMakeAMoveWhenGameIsntReady(){
		Move move = getMockMove();
		
		ResponseEntity<BattleshipErrorResponse> response =
				restTemplate.postForEntity(URL + "3/player/1/move", move, BattleshipErrorResponse.class);
			
		assertEquals(response.getBody().getMessage(), "You cannot make a move at the moment!");
		assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void shouldNotMakeAMoveWhenMoveIsAlreadPlayed(){
		Move move = getMockMove();
		move.setCoordinate(new Coordinate("A", 2));
		
		ResponseEntity<BattleshipErrorResponse> response =
				restTemplate.postForEntity(URL + "4/player/1/move", move, BattleshipErrorResponse.class);
			
		assertEquals(response.getBody().getMessage(), "You have already played on that coordinate!");
		assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void shouldNotMakeAMoveWhenIsNotYourTurn(){
		Move move = getMockMove();
		
		ResponseEntity<BattleshipErrorResponse> response =
				restTemplate.postForEntity(URL + "4/player/2/move", move, BattleshipErrorResponse.class);
			
		assertEquals(response.getBody().getMessage(), "It's not your turn!");
		assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void shouldGetLeaderboard(){
		@SuppressWarnings("rawtypes")
		ResponseEntity<List> response =
				restTemplate.getForEntity("/rest/player/leaderboard", List.class);
			
		assertEquals(response.getStatusCode(), HttpStatus.OK);
	}
	
	@Test
	public void shouldGetGameStatus(){
		ResponseEntity<Game> response =
				restTemplate.getForEntity(URL + "1/status", Game.class);
		
		assertNotNull(response.getBody().getId());
		assertEquals(response.getStatusCode(), HttpStatus.OK);
	}
	
	@Test
	public void shouldNotGetGameStatusWhenGameIsNotValid(){
		ResponseEntity<BattleshipErrorResponse> response =
				restTemplate.getForEntity(URL + "7/status", BattleshipErrorResponse.class);
		
		assertEquals(response.getBody().getMessage(), "Game not found!");
		assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
	}
	

	
	@Test
	public void shouldPauseGame(){
		ResponseEntity<Game> response =
				restTemplate.postForEntity(URL + "4/pause", null, Game.class);
			
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		assertEquals(response.getBody().getGameStatus(), GameStatus.PAUSED);
	}
	
	@Test
	public void shouldNotPauseGameWhenGameIsNotInProgress(){
		ResponseEntity<BattleshipErrorResponse> response =
				restTemplate.postForEntity(URL + "5/pause", null, BattleshipErrorResponse.class);
			
		assertEquals(response.getBody().getMessage(), "Game is not in progress!");
		assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void shouldNotPauseGameWhenGameIsNotValid(){
		ResponseEntity<BattleshipErrorResponse> response =
				restTemplate.postForEntity(URL + "6/pause", null, BattleshipErrorResponse.class);
			
		assertEquals(response.getBody().getMessage(), "Game not found!");
		assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
	}
	
	@Test
	public void shouldResumeGame(){
		ResponseEntity<Game> response =
				restTemplate.postForEntity(URL + "5/resume", null, Game.class);
			
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		assertEquals(response.getBody().getGameStatus(), GameStatus.IN_PROGRESS);
	}
	
	@Test
	public void shouldNotResumeGameWhenGameIsNotPaused(){
		ResponseEntity<BattleshipErrorResponse> response =
				restTemplate.postForEntity(URL + "4/resume", null, BattleshipErrorResponse.class);
			
		assertEquals(response.getBody().getMessage(), "Game is not paused!");
		assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void shouldNotResumeGameWhenGameIsNotValid(){
		ResponseEntity<BattleshipErrorResponse> response =
				restTemplate.postForEntity(URL + "6/resume", null, BattleshipErrorResponse.class);
			
		assertEquals(response.getBody().getMessage(), "Game not found!");
		assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
	}
	
	private Move getMockMove(){
		Move move = new Move();
		move.setCoordinate(new Coordinate("A", 1));
		
		return move;
	}
	
	private BoardPlacement getMockBoardPlacement(){
		BoardPlacement boardPlacement = new BoardPlacement();
		boardPlacement.setInitialCoordinate(new Coordinate("J", 5));
		boardPlacement.setDirection(Direction.HORIZONTAL);
		boardPlacement.setShip(Ships.BATTLESHIP);
		return boardPlacement;
	}
	
}
