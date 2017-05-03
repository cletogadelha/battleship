package com.cletogadelha.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;


@Entity
public class Coordinate extends AbstractBaseEntity {
	
	private static final long serialVersionUID = 1L;

	@NotNull
	@Column(nullable = false)
	private String letter;
	
	@NotNull
	@Column(nullable = false)
	private Integer number;
	
	public Coordinate(String letter, Integer number){
		this.letter = letter;
		this.number = number;
	}

	public String getLetter() {
		return letter;
	}

	public void setLetter(String letter) {
		this.letter = letter;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}
	
}
