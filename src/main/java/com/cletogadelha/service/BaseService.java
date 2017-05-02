package com.cletogadelha.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.cletogadelha.repository.BattleshipRepository;

import lombok.Data;

@Data
public abstract class BaseService<T> {
	
	@Autowired
    private BattleshipRepository<T, UUID> repository;
		
	public Iterable<T> getAll(){
		return repository.findAll();
	}
	
	public T create(T type){
		return repository.save(type);
	}
	
	public T get(UUID id){
		return repository.findOne(id);
	}
	
	public T update(T type){
		return repository.save(type);
	}
	
	public void delete(UUID id){
		repository.delete(id);
	}

}
