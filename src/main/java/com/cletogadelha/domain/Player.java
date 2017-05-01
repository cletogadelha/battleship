package com.cletogadelha.domain;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.Data;


@Entity
@Data
public class Player extends AbstractBaseEntity {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private UUID id;

	@Column
	private String name;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="board_id")
	private Board board;
	
}
