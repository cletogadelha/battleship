package com.cletogadelha.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cletogadelha.domain.Board;
import com.cletogadelha.domain.BoardPlacement;
import com.cletogadelha.domain.Coordinate;
import com.cletogadelha.domain.enums.Direction;

@Service
public class BoardService extends BaseService<Board> {
	
	@Autowired
	private CoordinateService coordinateService;
	
	/**
	 * Return all the coordinates that the ship will occupy starting from the initial coordinate
	 * @param placement
	 * @return
	 */
	public List<Coordinate> getAllCoordinatesFromInitialCoordinate(BoardPlacement placement) {
		
		List<Coordinate> coordinates = new ArrayList<>();

		Direction direction = placement.getDirection();
		
		for (int i = 0; i < placement.getShip().getSize(); i++) {
			if(Direction.HORIZONTAL.equals(direction)){
				coordinates.add(coordinateService.findByLetterAndNumber(placement.getInitialCoordinate().getLetter(), 
						placement.getInitialCoordinate().getNumber()+i));
			}else{
				coordinates.add(coordinateService.findByLetterAndNumber(String.valueOf((char)(placement.getInitialCoordinate().getLetter().charAt(0)+i)), 
						placement.getInitialCoordinate().getNumber()));
			}
		}
		
		return coordinates;
	}
	
}
