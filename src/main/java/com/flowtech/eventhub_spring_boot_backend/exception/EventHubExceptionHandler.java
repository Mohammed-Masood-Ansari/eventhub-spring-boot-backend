package com.flowtech.eventhub_spring_boot_backend.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class EventHubExceptionHandler {

	
	@ExceptionHandler(value = RuntimeException.class)
	public ResponseEntity<?> runTimeExcetionHandler(RuntimeException exception){
		return ResponseEntity.ok(exception.getMessage());
	}
}
