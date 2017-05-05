package com.cletogadelha.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.cletogadelha.repository.BattleshipRepository;

public abstract class BaseService<T> {
	
	@Autowired
    private BattleshipRepository<T, Integer> repository;
		
	public Iterable<T> getAll(){
		return repository.findAll();
	}
	
	public T create(T type){
		return repository.save(type);
	}
	
	public T get(Integer id){
		return repository.findOne(id);
	}
	
	public T update(T type){
		return repository.save(type);
	}
	
	public void delete(Integer id){
		repository.delete(id);
	}

	public BattleshipRepository<T, Integer> getRepository() {
		return repository;
	}

	public void setRepository(BattleshipRepository<T, Integer> repository) {
		this.repository = repository;
	}
	
	

}
