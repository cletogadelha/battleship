package com.cletogadelha.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.cletogadelha.domain.enums.Direction;
import com.cletogadelha.domain.enums.Ships;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class, property = "hash")
public class BoardPlacement extends AbstractBaseEntity {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Integer id;
	
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
	private List<Coordinate> filledCoordinates;
	
	@Column
	private Integer damage;
	
    @PrePersist
    protected void onCreate() {
    	super.onCreate();
    	damage = 0;
    }
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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

	public List<Coordinate> getFilledCoordinates() {
		return filledCoordinates;
	}

	public void setFilledCoordinates(List<Coordinate> filledCoordinates) {
		this.filledCoordinates = filledCoordinates;
	}

	public Coordinate getInitialCoordinate() {
		return initialCoordinate;
	}

	public void setInitialCoordinate(Coordinate initialCoordinate) {
		this.initialCoordinate = initialCoordinate;
	}

	public Integer getDamage() {
		return damage;
	}

	public void setDamage(Integer damage) {
		this.damage = damage;
	}

}
