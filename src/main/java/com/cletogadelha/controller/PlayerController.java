package com.cletogadelha.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cletogadelha.domain.Player;

@RestController
@RequestMapping("/rest/player")
public class PlayerController extends CRUDController<Player>{

}
