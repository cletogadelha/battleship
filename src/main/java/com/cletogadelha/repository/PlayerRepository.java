package com.cletogadelha.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cletogadelha.domain.Player;

public interface PlayerRepository extends JpaRepository<Player, UUID>{

}
