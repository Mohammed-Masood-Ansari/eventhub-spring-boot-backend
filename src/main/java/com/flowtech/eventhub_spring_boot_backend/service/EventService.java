package com.flowtech.eventhub_spring_boot_backend.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.flowtech.eventhub_spring_boot_backend.entity.Event;
import com.flowtech.eventhub_spring_boot_backend.enums.EventVerification;
import com.flowtech.eventhub_spring_boot_backend.repository.EventRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventService {

	private final EventRepository eventRepository;
	
	public ResponseEntity<?> registerEvent(Event event){
		
		System.out.println("Event received in service: " + event);
		
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
