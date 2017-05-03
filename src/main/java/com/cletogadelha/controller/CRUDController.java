package com.cletogadelha.controller;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cletogadelha.service.BaseService;

public abstract class CRUDController<T> {

	@Autowired
    private BaseService<T> service;

	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public T getDetail(@PathVariable("id") UUID id) {
		return service.get(id);
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public Iterable<T> getList() {
		return service.getAll();
	}
	
	@RequestMapping(method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public T create(@RequestBody @Valid T type){
		return service.create(type);
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.PUT)
	public T update(@PathVariable("id") Integer id, @RequestBody @Valid T type) {
		return service.update(type);
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<HttpStatus> remove(@PathVariable("id") UUID id){
		service.delete(id);
		return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
	}

	public BaseService<T> getService() {
		return service;
	}

	public void setService(BaseService<T> service) {
		this.service = service;
	}
    

}
