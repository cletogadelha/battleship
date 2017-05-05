package com.cletogadelha.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.StringIdGenerator.class, property = "hash")
public class Board extends AbstractBaseEntity {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name = "BOARD_ID", unique = true, nullable = false)
	private Integer id;
	
	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="BOARD_ID")
	private Set<BoardPlacement> boardPlacements;
	
	@Column(columnDefinition = "BOOLEAN DEFAULT FALSE", nullable = false)
	private Boolean finishedPlacement;
	
	@OneToMany(mappedBy = "pk.board", cascade=CascadeType.ALL)
	private Set<GamePlayerBoard> playerBoard = new HashSet<>();
	
	@PrePersist
	protected void onCreate(){
		super.onCreate();
		finishedPlacement = Boolean.FALSE;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Set<BoardPlacement> getBoardPlacements() {
		return boardPlacements;
	}

	public void setBoardPlacements(Set<BoardPlacement> boardPlacements) {
		this.boardPlacements = boardPlacements;
	}

	public Boolean isFinishedPlacement() {
		return finishedPlacement;
	}

	public void setFinishedPlacement(Boolean finishedPlacement) {
		this.finishedPlacement = finishedPlacement;
	}

	public Set<GamePlayerBoard> getPlayerBoard() {
		return playerBoard;
	}

	public void setPlayerBoard(Set<GamePlayerBoard> playerBoard) {
		this.playerBoard = playerBoard;
	}

}
