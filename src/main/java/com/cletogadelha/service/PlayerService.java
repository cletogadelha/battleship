package com.cletogadelha.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import com.cletogadelha.domain.Player;

@Service
public class PlayerService extends BaseService<Player> {
	
	public Page<Player> getLeaderBoard(){
		PageRequest top10 = new PageRequest(0, 10, new Sort(new Order(Direction.DESC, "score")));
		return getRepository().findAll(top10);
	}
	
}

