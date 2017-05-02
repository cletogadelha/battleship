package com.cletogadelha.domain;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class Move extends AbstractBaseEntity {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private UUID id;

	@Column
	private Coordenate coordenate;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Coordenate getCoordenate() {
		return coordenate;
	}

	public void setCoordenate(Coordenate coordenate) {
		this.coordenate = coordenate;
	}

}
