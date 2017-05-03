package com.cletogadelha.domain;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
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
	
	@Transient
	private Coordinate initialCoordinate;
	
	@OneToMany(fetch=FetchType.EAGER)
	@JoinColumn(name="BOARD_ID")
	private Coordinate coordinate;
	
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

	public Coordinate getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(Coordinate coordinate) {
		this.coordinate = coordinate;
	}

	public Coordinate getInitialCoordinate() {
		return initialCoordinate;
	}

	public void setInitialCoordinate(Coordinate initialCoordinate) {
		this.initialCoordinate = initialCoordinate;
	}

}
