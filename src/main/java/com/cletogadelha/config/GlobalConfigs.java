package com.cletogadelha.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

@Component
public class GlobalConfigs {
	
	@Bean
	public Module hibernate5Module()
	{
	    return new Hibernate5Module();
	}

}
