package com.cletogadelha.domain;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import com.cletogadelha.domain.enums.Direction;
import com.cletogadelha.domain.enums.Ships;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class, property = "@id")
public class BoardPlacement extends AbstractBaseEntity {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private UUID id;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private Ships ship;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private Direction direction;
	
	@NotNull
	@Column(nullable = false)
	private Coordenate coordenate;
	
	@Column
	private String[] filledSpaces;
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Ships getShip() {
		return ship;
	}

	public void setShip(Ships ship) {
		this.ship = ship;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public Coordenate getCoordenate() {
		return coordenate;
	}

	public void setCoordenate(Coordenate coordenate) {
		this.coordenate = coordenate;
	}

	public String[] getFilledSpaces() {
		return filledSpaces;
	}

	public void setFilledSpaces(String[] filledSpaces) {
		this.filledSpaces = filledSpaces;
	}
	
}
