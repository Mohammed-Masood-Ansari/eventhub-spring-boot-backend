package com.flowtech.eventhub_spring_boot_backend.exception;

public class TicketNotAvailableException extends RuntimeException {
	
	public TicketNotAvailableException(String message) {
		super(message);
	}

}
