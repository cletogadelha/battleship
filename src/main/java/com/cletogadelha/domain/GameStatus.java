package com.cletogadelha.domain;

public enum GameStatus {
	
	WAITING(1),
	IN_PROGRESS(2),
	PAUSED(3),
	FINISHED(4);
	
	private int cod;
	
	private GameStatus(int cod){
		this.cod = cod;
	}
	
	public int getCod(){
		return this.cod;
	}

}
