package com.cletogadelha.domain.enums;

public enum Ships {
	
	AIRCRAFT_CARRIE(5),
	BATTLESHIP(4),
	CRUISER(3),
	DESTROYER(2),
	SUBMARINE(1);
	
	private int size;
	
	private Ships(int size){
		this.size = size;
	}
	
	public int getSize(){
		return this.size;
	}

}
