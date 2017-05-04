package com.cletogadelha.domain;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.validation.constraints.Size;

import com.cletogadelha.domain.enums.GameStatus;

@Entity
public class Game extends AbstractBaseEntity {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "GAME_ID", unique = true, nullable = false)
	private UUID id;
	
	@Enumerated(EnumType.STRING)
	private GameStatus gameStatus;
	
	@OneToMany
	@JoinColumn(name="GAME_ID")
	private Set<Move> moves;
	
	@OneToMany(mappedBy = "pk.game", cascade = CascadeType.ALL)
	@Size(max = 2)
	private Set<GamePlayerBoard> playersOnGame = new HashSet<>();
	
	@OneToOne
	@JoinColumn(name="TURN_HOLDER")
	private Player turnHolder;
	
	@OneToOne
	@JoinColumn(name="PLAYER_WINNER")
	private Player winner;
	
	@Override
    @PrePersist
    protected void onCreate() {
		super.onCreate();
    	gameStatus = GameStatus.WAITING_OPPONENT;
    }

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public GameStatus getGameStatus() {
		return gameStatus;
	}

	public void setGameStatus(GameStatus gameStatus) {
		this.gameStatus = gameStatus;
	}

	public Set<Move> getMoves() {
		return moves;
	}

	public void setMoves(Set<Move> moves) {
		this.moves = moves;
	}

	public Set<GamePlayerBoard> getPlayersOnGame() {
		return playersOnGame;
	}

	public void setPlayersOnGame(Set<GamePlayerBoard> playersOnGame) {
		this.playersOnGame = playersOnGame;
	}

	public Player getTurnHolder() {
		return turnHolder;
	}

	public void setTurnHolder(Player turnHolder) {
		this.turnHolder = turnHolder;
	}

	public Player getWinner() {
		return winner;
	}

	public void setWinner(Player winner) {
		this.winner = winner;
	}

}
