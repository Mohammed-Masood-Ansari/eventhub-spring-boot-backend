package com.flowtech.eventhub_spring_boot_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
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
	public ResponseEntity<?> registerEvent(@RequestBody Event event,Authentication authentication) {

		System.out.println("Event received in controller: " + event);

		return eventService.registerEvent(event,authentication);
	}

	@GetMapping("/test")
	public ResponseEntity<String> test() {

		return ResponseEntity.ok("JWT authentication successful for ORGANISER");
	}
}
