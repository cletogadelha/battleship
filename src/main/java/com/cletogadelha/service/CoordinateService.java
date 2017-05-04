package com.cletogadelha.service;

import org.springframework.stereotype.Service;

import com.cletogadelha.domain.Coordinate;
import com.cletogadelha.repository.CoordinateRepository;

@Service
public class CoordinateService extends BaseService<Coordinate> {
	
	public Coordinate findByLetterAndNumberIgnoreCase(String letter, Integer number){
		return ((CoordinateRepository)getRepository()).findByLetterAndNumber(letter, number);
	}
	
}
