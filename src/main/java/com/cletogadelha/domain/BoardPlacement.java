package com.cletogadelha.domain;

import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.cletogadelha.domain.enums.Direction;
import com.cletogadelha.domain.enums.Ships;

@Entity
public class BoardPlacement extends AbstractBaseEntity {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private UUID id;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private Ships ship;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private Direction direction;
	
	@Transient
	private Coordinate initialCoordinate;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "PLACEMENT_COORDENATE", 
		joinColumns = { @JoinColumn(name = "PLACEMENT_ID") }, 
		inverseJoinColumns = { @JoinColumn(name = "COORDENATE_ID") })
	private Set<Coordinate> filledCoordinates;
	
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

	public Set<Coordinate> getFilledCoordinates() {
		return filledCoordinates;
	}

	public void setFilledCoordinates(Set<Coordinate> filledCoordinates) {
		this.filledCoordinates = filledCoordinates;
	}

	public Coordinate getInitialCoordinate() {
		return initialCoordinate;
	}

	public void setInitialCoordinate(Coordinate initialCoordinate) {
		this.initialCoordinate = initialCoordinate;
	}

}
