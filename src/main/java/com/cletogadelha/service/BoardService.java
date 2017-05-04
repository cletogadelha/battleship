package com.cletogadelha.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.cletogadelha.domain.Board;
import com.cletogadelha.domain.BoardPlacement;
import com.cletogadelha.domain.Coordinate;
import com.cletogadelha.domain.enums.Direction;

@Service
public class BoardService extends BaseService<Board> {
	
	public Set<Coordinate> getAllCoordinatesFromInitialCoordinate(BoardPlacement placement) {
		
		Set<Coordinate> coordinates = new HashSet<>();

		Direction direction = placement.getDirection();
		
		//TODO put in order | return the db record and not a new entity
		for (int i = 0; i < placement.getShip().getSize(); i++) {
			if(Direction.HORIZONTAL.equals(direction)){
				coordinates.add(new Coordinate(placement.getInitialCoordinate().getLetter(), 
						placement.getInitialCoordinate().getNumber()+i));
			}else{
				coordinates.add(new Coordinate(String.valueOf((char)(placement.getInitialCoordinate().getLetter().charAt(0)+i)), 
						placement.getInitialCoordinate().getNumber()));
			}
		}
		
		return coordinates;
	}
	
	
	
	
}
