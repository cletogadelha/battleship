package com.cletogadelha.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BattleshipExceptionHandler {
	
	@ExceptionHandler(BattleshipException.class)
	public ResponseEntity<BattleshipErrorResponse> battleshipExceptionHandler(Exception ex) {
		HttpStatus status = ((BattleshipException)ex).getStatus() == null 
				? HttpStatus.BAD_REQUEST : ((BattleshipException)ex).getStatus();	
		Integer responseCode = status == null 
				? HttpStatus.BAD_REQUEST.value() : status.value();
		
		BattleshipErrorResponse error = new BattleshipErrorResponse();
		error.setErrorCode(responseCode);
		error.setMessage(ex.getMessage());
		
		return new ResponseEntity<BattleshipErrorResponse>(error, status);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<BattleshipErrorResponse> exceptionHandler(Exception ex) {
		BattleshipErrorResponse error = new BattleshipErrorResponse();
		
		error.setErrorCode(HttpStatus.BAD_REQUEST.value());
		error.setMessage("Please contact our support team.");
//		error.setMessage(ex.getMessage());
		
		return new ResponseEntity<BattleshipErrorResponse>(error, HttpStatus.BAD_REQUEST);
	}	

}
