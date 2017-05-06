package com.cletogadelha.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cletogadelha.domain.Player;
import com.cletogadelha.service.PlayerService;

@RestController
@RequestMapping("/rest/player")
public class PlayerController extends CRUDController<Player>{
	
	@RequestMapping(value="/leaderboard", method=RequestMethod.GET)
	public ResponseEntity<?> getLeaderBoard() {
		return ResponseEntity.ok(((PlayerService) getService()).getLeaderBoard().getBody().getContent()); 
	}

}
