package com.cletogadelha.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cletogadelha.domain.Game;

public interface GameRepository extends JpaRepository<Game, UUID>{

}
