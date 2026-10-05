package com.flowtech.eventhub_spring_boot_backend.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.flowtech.eventhub_spring_boot_backend.entity.Event;
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
}
