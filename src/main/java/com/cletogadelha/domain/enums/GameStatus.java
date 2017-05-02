package com.cletogadelha.domain.enums;

public enum GameStatus {
	
	WAITING_OPPONENT(1),
	SETUP_PHASE(2),
	IN_PROGRESS(3),
	PAUSED(4),
	FINISHED(5);
	
	private int cod;
	
	private GameStatus(int cod){
		this.cod = cod;
	}
	
	public int getCod(){
		return this.cod;
	}

}
