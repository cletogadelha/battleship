package com.cletogadelha.repository;

import com.cletogadelha.domain.Coordinate;

public interface CoordinateRepository extends BattleshipRepository<Coordinate, Integer>{

	Coordinate findByLetterIgnoreCaseAndNumber(String letter, Integer number);
	
}
