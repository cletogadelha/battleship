package com.cletogadelha.exception;

import org.springframework.http.HttpStatus;

public class BattleshipException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	private String errorMessage;
	
	private HttpStatus status;

	public String getErrorMessage() {
		return errorMessage;
	}
	
	public HttpStatus getStatus(){
		return status;
	}
	
	public BattleshipException(String errorMessage) {
		super(errorMessage);
		this.errorMessage = errorMessage;
	}

	public BattleshipException(String errorMessage, HttpStatus status) {
		super(errorMessage);
		this.errorMessage = errorMessage;
		this.status = status;
	}

	public BattleshipException() {
		super();
	}
}
