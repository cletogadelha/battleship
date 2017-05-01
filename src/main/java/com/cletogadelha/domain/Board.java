package com.cletogadelha.domain;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Board extends AbstractBaseEntity {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private UUID id;
	
	@Enumerated(EnumType.STRING)
	private Enum<Ships> ships;
	
	@Enumerated(EnumType.STRING)
	private Enum<Direction> direction;

}
