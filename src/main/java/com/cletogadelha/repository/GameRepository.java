package com.cletogadelha.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.cletogadelha.domain.Game;

public interface GameRepository extends BattleshipRepository<Game, UUID>, JpaSpecificationExecutor<Game>{

}
