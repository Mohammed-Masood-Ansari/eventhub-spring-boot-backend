package com.flowtech.eventhub_spring_boot_backend.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.flowtech.eventhub_spring_boot_backend.enums.EventVerification;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "events")
public class Event {

	@GeneratedValue(strategy = GenerationType.IDENTITY,generator = "event")
	@SequenceGenerator(sequenceName = "event",name = "event_seq",initialValue = 87871)
	@Id
	private Integer id;
	private String name;
	private String description;
	private String location;
	private LocalDateTime dateTime;
	@CreationTimestamp
	private LocalDate createdAt;
	
	@Enumerated(EnumType.STRING)
	private EventVerification status=EventVerification.PENDING;// pending, approved, rejected
	
	@ManyToOne
	@JoinColumn(name = "organiser_id")
	private User user;
}
