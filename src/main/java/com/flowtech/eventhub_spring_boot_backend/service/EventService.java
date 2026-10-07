package com.flowtech.eventhub_spring_boot_backend.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.flowtech.eventhub_spring_boot_backend.entity.Event;
import com.flowtech.eventhub_spring_boot_backend.enums.EventVerification;
import com.flowtech.eventhub_spring_boot_backend.repository.EventRepository;
import com.flowtech.eventhub_spring_boot_backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventService {

	private final EventRepository eventRepository;
	
    private final UserRepository repository;
	
	public ResponseEntity<?> registerEvent(Event event,Authentication authentication){
		
		System.out.println("Event received in service: " + event);
		
		String email=authentication.getName();
		
		System.out.println("Authenticated user: " + email);
		

	    String role = authentication.getAuthorities()
	            .stream()
	            .findFirst()
	            .map(authority -> authority.getAuthority())
	            .orElse(null);

		
	    if(role.equals("ROLE_ORGANISER")) {
	    	repository.findByEmail(email).ifPresent(user -> {
				event.setUser(user);
			});
	    }
	    
		Event dbEvent=eventRepository.save(event);
		
		return ResponseEntity.ok(dbEvent);
	}
	
	public List<Event> findEventByStatusPending() {
		return eventRepository.findByStatus(EventVerification.PENDING);
	}
	
	public Event find(Integer eventId) {
		return eventRepository.findById(eventId).orElse(null);
	}
	
	public List<Event> getAllApprovedEvents(){
		return eventRepository.findByStatus(EventVerification.APPROVED);
	}
	
	public List<Event> getAllRejectedEvents(){
		return eventRepository.findByStatus(EventVerification.REJECTED);
	}
}
