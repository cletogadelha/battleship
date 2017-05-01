package com.cletogadelha.domain;

import java.util.Set;
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

import lombok.Data;

@Entity
@Data
public class Game extends AbstractBaseEntity {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private UUID id;
	
	@OneToMany(fetch=FetchType.EAGER)
	@JoinColumn(name="player_id")
	private Set<Player> players;
	
	@Enumerated(EnumType.STRING)
	private Enum<GameStatus> gameStatus;
	
}
