package com.cletogadelha.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cletogadelha.domain.Board;

public interface BoardRepository extends JpaRepository<Board, UUID>{

}
