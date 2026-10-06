package com.flowtech.eventhub_spring_boot_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flowtech.eventhub_spring_boot_backend.entity.Event;
import com.flowtech.eventhub_spring_boot_backend.enums.EventVerification;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

	
  List<Event> findByStatus(EventVerification status);
}
