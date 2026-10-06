package com.flowtech.eventhub_spring_boot_backend.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.flowtech.eventhub_spring_boot_backend.entity.Event;
import com.flowtech.eventhub_spring_boot_backend.enums.EventVerification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {

	private final EventService eventService;

	public ResponseEntity<?> approveEvents() {

		List<Event> events = eventService.findEventByStatusPending();

		if (events.isEmpty()) {
			
			throw new RuntimeException("No events found with status PENDING");
		}

		for (Event event : events) {

			event.setStatus(EventVerification.APPROVED);

			eventService.registerEvent(event);
		}

		return ResponseEntity.ok("All pending events have been approved.");
	}
	
	public ResponseEntity<?> changeEventStatus(Integer eventId, String status) {
		
		Event event = eventService.find(eventId);
		
		if (event == null) {
			throw new RuntimeException("Event not found with ID: " + eventId);
		}
		
		EventVerification newStatus;
		
		try {
			newStatus = EventVerification.valueOf(status.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("Invalid status value: " + status);
		}
		
		event.setStatus(newStatus);
		
		eventService.registerEvent(event);
		
		return ResponseEntity.ok("Event status updated successfully.");
	}
}
