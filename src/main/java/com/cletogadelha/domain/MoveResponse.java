package com.cletogadelha.domain;

import java.io.Serializable;

import com.cletogadelha.domain.enums.MoveStatus;

public class MoveResponse implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Move move;
	private MoveStatus status;
	private Game game;
	
	public Move getMove() {
		return move;
	}
	public void setMove(Move move) {
		this.move = move;
	}
	public MoveStatus getStatus() {
		return status;
	}
	public void setStatus(MoveStatus status) {
		this.status = status;
	}
	public Game getGame() {
		return game;
	}
	public void setGame(Game game) {
		this.game = game;
	}
	
	

}
