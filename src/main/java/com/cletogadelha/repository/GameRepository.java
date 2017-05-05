package com.cletogadelha.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.cletogadelha.domain.Game;

public interface GameRepository extends BattleshipRepository<Game, Integer>, JpaSpecificationExecutor<Game>{

}
