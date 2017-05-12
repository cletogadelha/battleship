package com.cletogadelha.controller;

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
	public ResponseEntity<T> getDetail(@PathVariable("id") Integer id) {
		T entity = service.get(id);
		if(entity == null){
			return new ResponseEntity<T>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<T>(service.get(id), HttpStatus.OK);
	}	
	
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<Iterable<T>> getList() {
		Iterable<T> list = service.getAll();
		if(list == null){
			return new ResponseEntity<Iterable<T>>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Iterable<T>>(list, HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<T> create(@RequestBody @Valid T type) throws Exception {
		return new ResponseEntity<T>(service.create(type), HttpStatus.CREATED);
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.PUT)
	public ResponseEntity<T> update(@PathVariable("id") Integer id, @RequestBody @Valid T type) {
		return new ResponseEntity<T>(service.update(type), HttpStatus.OK);
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<HttpStatus> remove(@PathVariable("id") Integer id){
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
