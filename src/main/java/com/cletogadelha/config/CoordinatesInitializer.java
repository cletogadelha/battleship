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
	
	private String initialLetter = "A";
	private Integer initialNumber = 1;
 
	@Autowired
    private CoordinateService coordinateService;
 
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
    	List<Coordinate> coordinates = coordinateService.getRepository().findAll();
    	if(coordinates.isEmpty()){
    		for(int i = 0; i < gridSize; i++){
    			for(int j = 0; j < gridSize; j++){
    				coordinates.add(new Coordinate(String.valueOf((char)(initialLetter.charAt(0)+i)), 
    						initialNumber + j));
    			}
    		}
    		coordinateService.getRepository().save(coordinates);
    	}
    }
}
