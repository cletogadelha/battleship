package com.cletogadelha.service;

import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cletogadelha.domain.Board;
import com.cletogadelha.domain.BoardPlacement;
import com.cletogadelha.domain.Coordenate;
import com.cletogadelha.domain.Game;
import com.cletogadelha.domain.GamePlayerBoard;
import com.cletogadelha.domain.Player;
import com.cletogadelha.domain.enums.Direction;
import com.cletogadelha.domain.enums.GameStatus;
import com.cletogadelha.repository.specification.GameSpecification;

@Service
public class GameService extends BaseService<Game> {
	
	@Autowired
	private PlayerService playerService;
	
	@Autowired
	private BoardService boardService;
	
	@Transactional
	public Game createNewGame(UUID player1) {
		Game newGame = new Game();
		
		Player p1 = playerService.get(player1);
		Board newBoard = boardService.create(new Board());
		
		//creating composite key
		GamePlayerBoard gpb = new GamePlayerBoard();
		gpb.setBoard(newBoard);
		gpb.setPlayer(p1);
		gpb.setGame(newGame);
		
		//Add player to game
		newGame.getPlayersOnGame().add(gpb);
		
		//create game
		return this.create(newGame);
	}
	
	@Transactional
	public Game joinGame(UUID gameId, UUID player2) {
		
		Game updatedGame = null;
		
		Game currentGame = this.get(gameId);
		Player p2 = playerService.get(player2);
		
		if(gameIsValidToJoin(currentGame, p2)){
			Board opponentBoard = boardService.create(new Board());
			
			//creating composite key
			GamePlayerBoard gpb = new GamePlayerBoard();
			gpb.setBoard(opponentBoard);
			gpb.setPlayer(p2);
			gpb.setGame(currentGame);
			
			currentGame.getPlayersOnGame().add(gpb);
			currentGame.setGameStatus(GameStatus.SETUP_PHASE);
			
			updatedGame = this.update(currentGame);
		}
		
		return updatedGame;
	}
	
	@Transactional
	public Game setupShip(UUID gameId, UUID playerId, BoardPlacement boardPlacement){
		
		Game updatedGame = null;
		
		Game currentGame = getRepository().findOne(GameSpecification.byIdWithCompleteFetch(gameId));
		
		GamePlayerBoard gamePlayerBoard = currentGame.getPlayersOnGame().stream()
				.filter(gpb -> playerId.equals(gpb.getPlayer().getId()))
				.findAny().orElse(null);
		
		if(gameIsValidToSetup(currentGame, gamePlayerBoard)){
			Set<BoardPlacement> placements = gamePlayerBoard.getBoard().getBoardPlacements();
			
			if(positionToPlaceIsValid(placements, boardPlacement)){
				
			}
			
		}
		
		return updatedGame;
	}
	
	private boolean positionToPlaceIsValid(Set<BoardPlacement> placements, BoardPlacement boardPlacement) {
		
		int size = boardPlacement.getShip().getSize();
		Direction direction = boardPlacement.getDirection();
		Coordenate coordenate = boardPlacement.getCoordenate(); 
		
		placements.stream().forEach(placement -> {
			Arrays.stream(placement.getFilledSpaces()).anyMatch(coord -> coord.equals(coordenate.getLetter() + coordenate.getNumber()));
		});
		
		return false;
	}

	private boolean gameIsValidToSetup(Game game, GamePlayerBoard gamePlayerBoard) {
		
		// Game is not valid to setup if it doesn't exist, or the player doens't belong to the game
		// or if it's not on setup phase or the placement has finished
		if(game == null 
				|| gamePlayerBoard == null 
				|| !GameStatus.SETUP_PHASE.equals(game.getGameStatus())
				|| gamePlayerBoard.getBoard().isFinishedPlacement()){
			return false;
		}
		
		return true;
	}

	private boolean gameIsValidToJoin(Game game, Player player) {
		
		//Game is not valid to join if it doesn't exist, or if it's not waiting to another player to join
		//or if it's a game that you are already in.
		if(game == null 
				|| !GameStatus.WAITING_OPPONENT.equals(game.getGameStatus())
				|| game.getPlayersOnGame().iterator().next().getPlayer().getName().equals(player.getName())){
			return false;
		}
		
		return true;
	}
	
}
