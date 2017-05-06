package com.cletogadelha.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.cletogadelha.domain.Coordinate;
import com.cletogadelha.service.CoordinateService;

@Component
public class CoordinatesInitializer implements ApplicationListener<ContextRefreshedEvent> {
	
	@Value("${grid.size}")
	private Integer gridSize;
	
	private static final String INITIAL_LETTER = "A";
	private static final Integer INITIAL_NUMBER = 1;
 
	@Autowired
    private CoordinateService coordinateService;
 
	/**
	 * Method responsible to initialize all the coordinates used by the game
	 */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
    	List<Coordinate> coordinates = coordinateService.getRepository().findAll();
    	if(coordinates.isEmpty()){
    		for(int i = 0; i < gridSize; i++){
    			for(int j = 0; j < gridSize; j++){
    				coordinates.add(new Coordinate(String.valueOf((char)(INITIAL_LETTER.charAt(0)+i)), 
    						INITIAL_NUMBER + j));
    			}
    		}
    		coordinateService.getRepository().save(coordinates);
    	}
    }
}
