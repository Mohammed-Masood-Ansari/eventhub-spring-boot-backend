package com.flowtech.eventhub_spring_boot_backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.flowtech.eventhub_spring_boot_backend.entity.Event;
import com.flowtech.eventhub_spring_boot_backend.service.AdminService;
import com.flowtech.eventhub_spring_boot_backend.service.EventService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/admin")
@RequiredArgsConstructor
public class AdminController {

	private final AdminService adminService;
	
	private final EventService eventService;
	
	@GetMapping(value = "/approveEvents")
	public ResponseEntity<?> approveEvents(Authentication authentication) {
		
		return adminService.approveEvents(authentication);
	}
	
	@GetMapping(value = "/findEventByStatusPending")
	public List<Event> findEventByStatusPending() {
		
		return eventService.findEventByStatusPending();
	}
	
	@PostMapping(value = "/changeEventStatus")
	public ResponseEntity<?> changeEventStatus(@RequestParam Integer eventId, @RequestParam String status,Authentication authentication) {
		
		return adminService.changeEventStatus(eventId,status,authentication);
	}
}
