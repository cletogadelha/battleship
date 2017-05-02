package com.cletogadelha.domain;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
	
	@Enumerated(EnumType.STRING)
	private Enum<Ships> ship;
	
	@Enumerated(EnumType.STRING)
	private Enum<Direction> direction;
	
	@Column
	private Coord coordenate;
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Enum<Ships> getShip() {
		return ship;
	}

	public void setShip(Enum<Ships> ship) {
		this.ship = ship;
	}

	public Enum<Direction> getDirection() {
		return direction;
	}

	public void setDirection(Enum<Direction> direction) {
		this.direction = direction;
	}

	public Coord getCoordenate() {
		return coordenate;
	}

	public void setCoordenate(Coord coordenate) {
		this.coordenate = coordenate;
	}
	
}
