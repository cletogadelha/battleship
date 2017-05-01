package com.cletogadelha.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cletogadelha.domain.Game;

@RestController
@RequestMapping("/rest/game")
public class GameController extends CRUDController<Game>{

}
