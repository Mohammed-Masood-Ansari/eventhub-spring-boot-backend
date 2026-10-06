package com.flowtech.eventhub_spring_boot_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flowtech.eventhub_spring_boot_backend.entity.Event;
import com.flowtech.eventhub_spring_boot_backend.service.EventService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping(value = "/organiser")
@RequiredArgsConstructor
public class OrganiserController {

	private final EventService eventService;

	@PostMapping(value = "/registerEvent")
	public ResponseEntity<?> registerEvent(@RequestBody Event event) {

		System.out.println("Event received in controller: " + event);
		
		return eventService.registerEvent(event);
	}
}
