package com.cletogadelha.domain;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotNull;

@Entity
public class Player extends AbstractBaseEntity {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "PLAYER_ID", unique = true, nullable = false)
	private UUID id;

	@NotNull
	@Column(unique = true, nullable = false)
	private String name;
	
	@OneToMany(mappedBy = "pk.player")
	private Set<GamePlayerBoard> games = new HashSet<>();
	
	@Column
	private Integer wins;
	
	@Column
	private Integer losses;
	
	@Column
	private Integer score;
	
	@PrePersist
	protected void onCreate() {
		super.onCreate();
		wins = losses = score = 0;
	}

    @PreUpdate
    protected void onUpdate() {
    	super.onUpdate();
    	score = wins - losses;
    }
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<GamePlayerBoard> getGames() {
		return games;
	}

	public void setGames(Set<GamePlayerBoard> games) {
		this.games = games;
	}

	public Integer getWins() {
		return wins;
	}

	public void setWins(Integer wins) {
		this.wins = wins;
	}

	public Integer getLosses() {
		return losses;
	}

	public void setLosses(Integer losses) {
		this.losses = losses;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}
	
}
