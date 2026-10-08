package com.flowtech.eventhub_spring_boot_backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.flowtech.eventhub_spring_boot_backend.entity.Booking;
import com.flowtech.eventhub_spring_boot_backend.entity.Event;
import com.flowtech.eventhub_spring_boot_backend.service.EventService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/customer")
@RequiredArgsConstructor
public class CustomerController {

	private final EventService eventService;
	
	@GetMapping(value = "/getAllApprovedEvents")
	public List<Event> getAllApprovedEvents() {
		
		return eventService.getAllApprovedEvents();
	}
	
	@GetMapping(value = "/bookEvent")
	public ResponseEntity<?> bookEvent(@RequestParam int eventId,@RequestParam int ticketQuantity,Authentication authentication) {
		
		Booking booking=eventService.bookEvent(eventId,ticketQuantity,authentication);
		
		return ResponseEntity.ok(booking);
	}
	
}
