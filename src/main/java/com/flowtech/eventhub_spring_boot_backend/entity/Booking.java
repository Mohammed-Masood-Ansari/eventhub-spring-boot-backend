package com.flowtech.eventhub_spring_boot_backend.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;

@Entity
public class Booking {

	@Id
	@GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY, generator = "booking_seq")
	@SequenceGenerator(sequenceName = "booking", name = "booking_seq", initialValue = 5444332)
	private int bookingId;
	private int quantity;
	private double totalPrice;
	private String paymentStatus;
	private LocalDateTime paymentDateTime;
	private LocalDateTime eventDateTime;
	@CreationTimestamp
	private LocalDateTime bookingDateTime;
	
	
	@ManyToOne
	private Event event;
	
	@OneToOne
	private User user;
}
