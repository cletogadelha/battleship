package com.cletogadelha.domain.enums;

public enum Ships {
	
	AIRCRAFT_CARRIER(5),
	BATTLESHIP(4),
	CRUISER(3),
	SUBMARINE(3),
	DESTROYER(2);
	
	private int size;
	
	private Ships(int size){
		this.size = size;
	}
	
	public int getSize(){
		return this.size;
	}

}
