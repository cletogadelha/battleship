package com.cletogadelha.service;

import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cletogadelha.domain.Board;
import com.cletogadelha.domain.BoardPlacement;
import com.cletogadelha.domain.Coordinate;
import com.cletogadelha.domain.Game;
import com.cletogadelha.domain.GamePlayerBoard;
import com.cletogadelha.domain.Move;
import com.cletogadelha.domain.Player;
import com.cletogadelha.domain.enums.GameStatus;
import com.cletogadelha.repository.specification.GameSpecification;

@Service
public class GameService extends BaseService<Game> {
	
	@Autowired
	private PlayerService playerService;
	
	@Autowired
	private BoardService boardService;
	
	@Autowired
	private CoordinateService coordinateService;
	
	@Transactional
	public Game createNewGame(UUID player1) {
		Player p1 = playerService.get(player1);
		
		if(p1 == null){
			return null;
		}else{
			Game newGame = new Game();
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
	}
	
	@Transactional
	public Game joinGame(UUID gameId, UUID player2) {
		
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
		}
		
		return currentGame;
	}
	
	@Transactional
	public Game setupShip(UUID gameId, UUID playerId, BoardPlacement boardPlacement){
		
		Game currentGame = getRepository().findOne(GameSpecification.byIdWithSimpleFetch(gameId));
		
		GamePlayerBoard gamePlayerBoard = returnPlayerBoard(playerId, currentGame);
		
		if(gameIsValidToSetup(currentGame, gamePlayerBoard)){
			Set<BoardPlacement> placements = gamePlayerBoard.getBoard().getBoardPlacements();
			Set<Coordinate> coordinatesToBeFilled = 
					boardService.getAllCoordinatesFromInitialCoordinate(boardPlacement);
			
			if(shipIsValid(placements, boardPlacement) && positionToPlaceIsValid(placements, coordinatesToBeFilled)){
				
				boardPlacement.setFilledCoordinates(coordinatesToBeFilled);
				
				Set<BoardPlacement> userPlacements = gamePlayerBoard.getBoard().getBoardPlacements();
				userPlacements.add(boardPlacement);
				gamePlayerBoard.getBoard().setFinishedPlacement(userPlacements.size() == 5 ? true : false);
				
				validateIfGameIsReadyToStart(currentGame);
			}
		}
		
		return currentGame;
	}

	@Transactional
	public Game makeAMove(UUID gameId, UUID playerId, Move move){
		
		Game currentGame = get(gameId);
		Player playerMakingMove = playerService.get(playerId);
		
		if(playerCanMakeAMove(currentGame, playerMakingMove) 
				&& moveIsValid(playerMakingMove, currentGame, move)){
			Coordinate coord = coordinateService
					.findByLetterAndNumberIgnoreCase(move.getCoordinate().getLetter(), move.getCoordinate().getNumber());
			move.setCoordinate(coord);
			move.setPlayer(playerMakingMove);
			currentGame.getMoves().add(move);
			
			GamePlayerBoard opponentBoard = returnOpponentBoard(playerId, currentGame);
			
			if(shipWasHit(opponentBoard, move)){
				if(gameIsOver(opponentBoard)){
					playerMakingMove.setWins(playerMakingMove.getWins()+1);
					opponentBoard.getPlayer().setLosses(opponentBoard.getPlayer().getLosses()+1);
					
					currentGame.setWinner(playerMakingMove);
					
					currentGame.setGameStatus(GameStatus.FINISHED);
				}
			}
			changeTurnHolder(currentGame);
		}
		
		return currentGame;
	}
	
	@Transactional
	public Game coinFlip(UUID gameId){
		Game currentGame = get(gameId);
		if(gameIsReadyToCoinFlip(currentGame)){
			int randomIndex = new Random().nextInt(2);
			Iterator<GamePlayerBoard> iter = currentGame.getPlayersOnGame().iterator();
			for (int i = 0; i < randomIndex; i++) {
			    iter.next();
			}
			currentGame.setTurnHolder(iter.next().getPlayer());
			currentGame.setGameStatus(GameStatus.IN_PROGRESS);
		}
		return currentGame;
	}
	
	public Game gameStatus(UUID gameId){
		return getRepository().findOne(GameSpecification.byIdWithCompleteFetch(gameId));
	}
	
	private boolean gameIsReadyToCoinFlip(Game currentGame) {
		return currentGame.getGameStatus().equals(GameStatus.WAITING_COIN_FLIP);
	}

	private boolean gameIsOver(GamePlayerBoard currentGame) {
		
		return currentGame.getBoard().getBoardPlacements().stream()
				.allMatch(placement -> 
					placement.getDamage().equals(placement.getShip().getSize()));
	}

	private boolean moveIsValid(Player playerMakingMove, Game currentGame, Move move) {
		
		Set<Move> movesByUser = 
				currentGame.getMoves().stream()
					.filter(userMove -> userMove.getPlayer().getId().equals(playerMakingMove.getId()))
					.collect(Collectors.toSet());
 		
		for(Move movePlayed : movesByUser){
			if(movePlayed.getCoordinate().equals(move.getCoordinate())){
				return false;
			}
		}
		return true;
	}

	private boolean shipWasHit(GamePlayerBoard opponentBoard, Move move) {
		for(BoardPlacement placement : opponentBoard.getBoard().getBoardPlacements()){
			if(thereIsAShipOnMovePosition(placement, move)){
				placement.setDamage(placement.getDamage()+1);
				return true;
			}
		}
		return false;
	}
	
	private boolean thereIsAShipOnMovePosition(BoardPlacement placement, Move move){
		return placement.getFilledCoordinates().stream()
				.filter(coordinate -> coordinate.equals(move.getCoordinate()))
				.findAny().orElse(null) != null;
	}

	private GamePlayerBoard returnPlayerBoard(UUID playerId, Game currentGame) {
		return currentGame.getPlayersOnGame().stream()
				.filter(gpb -> playerId.equals(gpb.getPlayer().getId()))
				.findAny().orElse(null);
	}
	
	private GamePlayerBoard returnOpponentBoard(UUID playerId, Game currentGame) {
		return currentGame.getPlayersOnGame().stream()
				.filter(gpb -> !playerId.equals(gpb.getPlayer().getId()))
				.findAny().orElse(null);
	}
	
	private void changeTurnHolder(Game currentGame) {
		if(!currentGame.getGameStatus().equals(GameStatus.FINISHED)){
			GamePlayerBoard opponentBoard = currentGame.getPlayersOnGame().stream()
					.filter(gpb -> !currentGame.getTurnHolder().getId().equals(gpb.getPlayer().getId()))
					.findAny().orElse(null);
			
			currentGame.setTurnHolder(opponentBoard.getPlayer());
		}
	}
	
	private boolean playerCanMakeAMove(Game currentGame, Player playerMakingMove){
		
		if(currentGame == null 
				|| playerMakingMove == null 
				|| !currentGame.getGameStatus().equals(GameStatus.IN_PROGRESS)
				|| !currentGame.getTurnHolder().getId()
						.equals(playerMakingMove.getId())){
			return false;
		}
		
		return true;
	}
	
	private boolean shipIsValid(Set<BoardPlacement> placements, BoardPlacement boardPlacement) {
		return placements.stream()
				.filter(placement -> placement.getShip().equals(boardPlacement.getShip()))
				.findAny().orElse(null) == null;
	}

	private void validateIfGameIsReadyToStart(Game currentGame) {
		for(GamePlayerBoard gpb : currentGame.getPlayersOnGame()){
			if(!gpb.getBoard().isFinishedPlacement()){
				return;
			}
		}
		
		currentGame.setGameStatus(GameStatus.WAITING_COIN_FLIP);
	}

	private boolean positionToPlaceIsValid(Set<BoardPlacement> placements, Set<Coordinate> coordinatesToBeFilled) {
		
		//Validate Board Edges
		for(Coordinate coord : coordinatesToBeFilled) {
			if(coord == null || coord.getLetter().compareTo("J") > 0 || coord.getNumber().compareTo(10) > 0){
				return false;
			}
		};
		
		//Validate if any coordinate matches with one that is deployed on player's board
		for(BoardPlacement placement : placements){
			for(Coordinate coordinateToBeFilled : coordinatesToBeFilled){
				if(placement.getFilledCoordinates().contains(coordinateToBeFilled)){
					return false;
				}
			}
		}
		
		return true;
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
