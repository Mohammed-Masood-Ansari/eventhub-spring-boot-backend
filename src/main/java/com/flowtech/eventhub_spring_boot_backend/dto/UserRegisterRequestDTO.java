package com.flowtech.eventhub_spring_boot_backend.dto;

import lombok.Data;

@Data
public class UserRegisterRequestDTO {
	
	private String name;
	private String email;
	private String password;
	private Long phone;
	private String roleName;
	
}
