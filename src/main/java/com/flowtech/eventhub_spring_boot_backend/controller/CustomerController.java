package com.flowtech.eventhub_spring_boot_backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	
}
