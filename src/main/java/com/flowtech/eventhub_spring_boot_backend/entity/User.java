package com.flowtech.eventhub_spring_boot_backend.entity;

import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "users")
@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY,generator = "user")
	@SequenceGenerator(sequenceName = "user",name = "user_seq",initialValue = 5001)
	private Integer id;
	private String name;
	private String email;
	private String password;
	private Long phone;
	@CreationTimestamp
	private LocalDate createdAt;
	
	@ManyToOne
	@JoinColumn(name = "role-id")
	private Role role;
}
