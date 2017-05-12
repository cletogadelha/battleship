package com.cletogadelha.service;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.cletogadelha.domain.Board;
import com.cletogadelha.domain.BoardPlacement;
import com.cletogadelha.domain.Coordinate;
import com.cletogadelha.domain.Game;
import com.cletogadelha.domain.GamePlayerBoard;
import com.cletogadelha.domain.Move;
import com.cletogadelha.domain.MoveResponse;
import com.cletogadelha.domain.Player;
import com.cletogadelha.domain.ShipStatus;
import com.cletogadelha.domain.enums.GameStatus;
import com.cletogadelha.domain.enums.MoveStatus;
import com.cletogadelha.domain.enums.ShipStatusEnum;
import com.cletogadelha.exception.BattleshipException;
import com.cletogadelha.repository.specification.GameSpecification;

@Service
public class GameService extends BaseService<Game> {
	
	@Autowired
	private PlayerService playerService;
	
	@Autowired
	private BoardService boardService;
	
	@Autowired
	private CoordinateService coordinateService;
	
	/**
	 * Method responsible to create a new game and a board for the player
	 * @param player1
	 * @return
	 * @throws BattleshipException
	 */
	@Transactional
	public Game createNewGame(Integer player1) throws BattleshipException {
		Player p1 = playerService.get(player1);
		
		if(p1 == null){
			throw new BattleshipException("Player not found!", HttpStatus.NOT_FOUND);
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
	
	/**
	 * Method responsible to insert another player in an existing game, creating a board for him
	 * @param gameId
	 * @param player2
	 * @return
	 * @throws BattleshipException
	 */
	@Transactional
	public Game joinGame(Integer gameId, Integer player2) throws BattleshipException {
		
		Game currentGame = this.get(gameId);
		Player p2 = playerService.get(player2);
		if(p2 == null){
			throw new BattleshipException("Player not found!", HttpStatus.NOT_FOUND);
		}else{
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
		}
		return currentGame;
	}
	
	/**
	 * Method responsible to setup a ship on the board
	 * @param gameId
	 * @param playerId
	 * @param boardPlacement
	 * @return
	 * @throws BattleshipException
	 */
	@Transactional
	public Game setupShip(Integer gameId, Integer playerId, BoardPlacement boardPlacement) throws BattleshipException {
		
		Game currentGame = getRepository().findOne(GameSpecification.byIdWithSimpleFetch(gameId));
		
		GamePlayerBoard gamePlayerBoard = returnPlayerBoard(playerId, currentGame);
		
		if(gameIsValidToSetup(currentGame, gamePlayerBoard)){
			Set<BoardPlacement> placements = gamePlayerBoard.getBoard().getBoardPlacements();
			List<Coordinate> coordinatesToBeFilled = 
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

	/**
	 * Method responsible to make a move in the game
	 * @param gameId
	 * @param playerId
	 * @param move
	 * @return
	 * @throws BattleshipException
	 */
	@Transactional
	public MoveResponse makeAMove(Integer gameId, Integer playerId, Move move) throws BattleshipException {
		
		MoveResponse response = new MoveResponse();
		
		Game currentGame = getRepository().findOne(GameSpecification.byIdWithCompleteFetch(gameId));
		Player playerMakingMove = playerService.get(playerId);
		
		if(playerCanMakeAMove(currentGame, playerMakingMove) 
				&& moveIsValid(playerMakingMove, currentGame, move)){
			Coordinate coord = coordinateService
					.findByLetterAndNumber(move.getCoordinate().getLetter(), move.getCoordinate().getNumber());
			move.setCoordinate(coord);
			move.setPlayer(playerMakingMove);
			currentGame.getMoves().add(move);
			
			response.setMove(move);
			response.setGame(currentGame);
			
			GamePlayerBoard opponentBoard = returnOpponentBoard(playerId, currentGame);
			
			ShipStatus shipSatus = null;
			if((shipSatus = shipWasHit(opponentBoard, move)) != null){
				response.setStatus(MoveStatus.HIT);
				response.setShipStatus(shipSatus);
				
				if(gameIsOver(opponentBoard)){
					response.setStatus(MoveStatus.WON);
					playerMakingMove.setWins(playerMakingMove.getWins()+1);
					opponentBoard.getPlayer().setLosses(opponentBoard.getPlayer().getLosses()+1);
					
					currentGame.setWinner(playerMakingMove);
					
					currentGame.setGameStatus(GameStatus.FINISHED);
				}
			}else{
				response.setStatus(MoveStatus.MISSED);
			}
			
			changeTurnHolder(currentGame);
		}
		
		return response;
	}
	
	/**
	 * Method responsible to determine who is going to be the first player to make a move
	 * @param gameId
	 * @return
	 * @throws BattleshipException
	 */
	@Transactional
	public Game flipCoin(Integer gameId) throws BattleshipException {
		Game currentGame = getRepository().findOne(GameSpecification.byIdWithCompleteFetch(gameId));
		if(gameIsReadyToFlipCoin(currentGame)){
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
	
	/**
	 * Method responsible for get a Game and all your state
	 * @param gameId
	 * @return
	 * @throws BattleshipException
	 */
	public Game gameStatus(Integer gameId) throws BattleshipException {
		Game game = getRepository().findOne(GameSpecification.byIdWithCompleteFetch(gameId));
		
		if(game == null){
			throw new BattleshipException("Game not found!", HttpStatus.NOT_FOUND);
		}
				
		return game;
	}
	
	/**
	 * Method for pause a game
	 * @param gameId
	 * @return
	 * @throws BattleshipException
	 */
	@Transactional
	public Game pauseGame(Integer gameId) throws BattleshipException {
		Game game = get(gameId);
		if(game == null){
			throw new BattleshipException("Game not found!", HttpStatus.NOT_FOUND);
		}
		
		if(!GameStatus.IN_PROGRESS.equals(game.getGameStatus())){
			throw new BattleshipException("Game is not in progress!");
		}
		
		game.setGameStatus(GameStatus.PAUSED);
		
		return game;
	}
	
	/**
	 * Method responsible to resume a paused game
	 * @param gameId
	 * @return
	 * @throws BattleshipException
	 */
	@Transactional
	public Game resumeGame(Integer gameId) throws BattleshipException {
		Game game = get(gameId);
		if(game == null){
			throw new BattleshipException("Game not found!", HttpStatus.NOT_FOUND);
		}
		
		if(!GameStatus.PAUSED.equals(game.getGameStatus())){
			throw new BattleshipException("Game is not paused!");
		}
		
		game.setGameStatus(GameStatus.IN_PROGRESS);
		
		return game;
	}
	
	/**
	 * Validate if the game is ready to flip the coin
	 * @param currentGame
	 * @return
	 * @throws BattleshipException
	 */
	private boolean gameIsReadyToFlipCoin(Game currentGame) throws BattleshipException {
		if(!currentGame.getGameStatus().equals(GameStatus.WAITING_FLIP_COIN)){
			throw new BattleshipException("The game is not at flip coin phase! Let's wait until the players finish the deployment");
		}
		return true;
	}

	/**
	 * Validate if all the ships are destroyed (full damage)
	 * @param currentGame
	 * @return
	 */
	private boolean gameIsOver(GamePlayerBoard currentGame) {
		
		return currentGame.getBoard().getBoardPlacements().stream()
				.allMatch(placement -> 
					placement.getDamage().equals(placement.getShip().getSize()));
	}

	/**
	 * Validate if the Move was already played in the game
	 * @param playerMakingMove
	 * @param currentGame
	 * @param move
	 * @return
	 * @throws BattleshipException
	 */
	private boolean moveIsValid(Player playerMakingMove, Game currentGame, Move move) throws BattleshipException {
		
		Set<Move> movesByUser = 
				currentGame.getMoves().stream()
					.filter(userMove -> userMove.getPlayer().getId().equals(playerMakingMove.getId()))
					.collect(Collectors.toSet());
 		
		for(Move movePlayed : movesByUser){
			if(movePlayed.getCoordinate().equals(move.getCoordinate())){
				throw new BattleshipException("You have already played on that coordinate!");
			}
		}
		return true;
	}

	/**
	 * Validate if a ship was hit
	 * @param opponentBoard
	 * @param move
	 * @return
	 */
	private ShipStatus shipWasHit(GamePlayerBoard opponentBoard, Move move) {
		ShipStatus shipStatus = null;
		
		for(BoardPlacement placement : opponentBoard.getBoard().getBoardPlacements()){
			if(thereIsAShipOnMovePosition(placement, move)){
				placement.setDamage(placement.getDamage()+1);
				
				shipStatus = new ShipStatus();
				shipStatus.setShip(placement.getShip());
				shipStatus.setStatus( placement.getShip().getSize() == placement.getDamage()  ? ShipStatusEnum.SUNK :  ShipStatusEnum.DAMAGED);
				
				return shipStatus;
			}
		}
		return shipStatus;
	}
	
	/**
	 * Validate if there is a ship on the Move Position
	 * @param placement
	 * @param move
	 * @return
	 */
	private boolean thereIsAShipOnMovePosition(BoardPlacement placement, Move move) {
		return placement.getFilledCoordinates().stream()
				.filter(coordinate -> coordinate.equals(move.getCoordinate()))
				.findAny().orElse(null) != null;
	}

	/**
	 * Return the player board
	 * @param playerId
	 * @param currentGame
	 * @return
	 */
	private GamePlayerBoard returnPlayerBoard(Integer playerId, Game currentGame) {
		return currentGame.getPlayersOnGame().stream()
				.filter(gpb -> playerId.equals(gpb.getPlayer().getId()))
				.findAny().orElse(null);
	}
	
	/**
	 * Return the opponent board
	 * @param playerId
	 * @param currentGame
	 * @return
	 */
	private GamePlayerBoard returnOpponentBoard(Integer playerId, Game currentGame) {
		return currentGame.getPlayersOnGame().stream()
				.filter(gpb -> !playerId.equals(gpb.getPlayer().getId()))
				.findAny().orElse(null);
	}
	
	/**
	 * Method responsible for change change the player holding the turn
	 * @param currentGame
	 */
	private void changeTurnHolder(Game currentGame) {
		if(!currentGame.getGameStatus().equals(GameStatus.FINISHED)){
			GamePlayerBoard opponentBoard = currentGame.getPlayersOnGame().stream()
					.filter(gpb -> !currentGame.getTurnHolder().getId().equals(gpb.getPlayer().getId()))
					.findAny().orElse(null);
			
			currentGame.setTurnHolder(opponentBoard.getPlayer());
		}
	}
	
	/**
	 * Validate if a Move is valid
	 * @param currentGame
	 * @param playerMakingMove
	 * @return
	 * @throws BattleshipException
	 */
	private boolean playerCanMakeAMove(Game currentGame, Player playerMakingMove) throws BattleshipException {
		
		if(currentGame == null 
				|| playerMakingMove == null 
				|| !currentGame.getGameStatus().equals(GameStatus.IN_PROGRESS)){
			throw new BattleshipException("You cannot make a move at the moment!");
		}else if(!currentGame.getTurnHolder().getId().equals(playerMakingMove.getId())){
			throw new BattleshipException("It's not your turn!");
		}
		
		return true;
	}
	
	/**
	 * Validate if the ship is already deployed on player's board
	 * @param placements
	 * @param boardPlacement
	 * @return
	 * @throws BattleshipException
	 */
	private boolean shipIsValid(Set<BoardPlacement> placements, BoardPlacement boardPlacement) throws BattleshipException {
		if(placements.stream()
				.filter(placement -> placement.getShip().equals(boardPlacement.getShip()))
				.findAny().orElse(null) != null){
			throw new BattleshipException("You have already deployed this kind of ship!");
		}
		return true;
	}

	/**
	 * Method responsible to validate if the setup phase is over
	 * @param currentGame
	 * @throws BattleshipException
	 */
	private void validateIfGameIsReadyToStart(Game currentGame) throws BattleshipException {
		for(GamePlayerBoard gpb : currentGame.getPlayersOnGame()){
			if(!gpb.getBoard().isFinishedPlacement()){
				return;
			}
		}
		
		currentGame.setGameStatus(GameStatus.WAITING_FLIP_COIN);
	}

	/**
	 * Method responsible to validate if the position is valid
	 * @param placements
	 * @param coordinatesToBeFilled
	 * @return
	 * @throws BattleshipException
	 */
	private boolean positionToPlaceIsValid(Set<BoardPlacement> placements, List<Coordinate> coordinatesToBeFilled) throws BattleshipException {
		
		//Validate if any coordinate is out of the board range
		for(Coordinate coord : coordinatesToBeFilled) {
			if(coord == null){
				throw new BattleshipException("You cannot place a ship on that position!");
			}
		};
		
		//Validate if any coordinate matches with one that is deployed on player's board
		for(BoardPlacement placement : placements){
			for(Coordinate coordinateToBeFilled : coordinatesToBeFilled){
				if(placement.getFilledCoordinates().contains(coordinateToBeFilled)){
					throw new BattleshipException("Oops! There is a ship deployed in here!");
				}
			}
		}
		
		return true;
	}

	/**
	 * Validate if a player can setup a ship
	 * @param game
	 * @param gamePlayerBoard
	 * @return
	 * @throws BattleshipException
	 */
	private boolean gameIsValidToSetup(Game game, GamePlayerBoard gamePlayerBoard) throws BattleshipException {
		
		// Game is not valid to setup if it doesn't exist, or the player doens't belong to the game
		// or if it's not on setup phase or the placement has finished
		if(game == null 
				|| gamePlayerBoard == null 
				|| !GameStatus.SETUP_PHASE.equals(game.getGameStatus())){
			throw new BattleshipException("Sorry! You cannot setup ships in this game!");
		}else if(gamePlayerBoard.getBoard().isFinishedPlacement()){
			throw new BattleshipException("The setup phase is already finished!");
		}
		
		return true;
	}

	/**
	 * Validate if a player can join a game
	 * @param game
	 * @param player
	 * @return
	 * @throws BattleshipException
	 */
	private boolean gameIsValidToJoin(Game game, Player player) throws BattleshipException {
		
		//Game is not valid to join if it doesn't exist, or if it's not waiting to another player to join
		//or if it's a game that you are already in.
		if(game == null){
			throw new BattleshipException("Game not found!", HttpStatus.NOT_FOUND);
		}else if(!GameStatus.WAITING_OPPONENT.equals(game.getGameStatus())){
			throw new BattleshipException("Sorry! This game is not available to enter!");
		}else if(game.getPlayersOnGame().iterator().next().getPlayer().getName().equals(player.getName())){
			throw new BattleshipException("You are already in this game!");
		}
		
		return true;
	}
	
}
