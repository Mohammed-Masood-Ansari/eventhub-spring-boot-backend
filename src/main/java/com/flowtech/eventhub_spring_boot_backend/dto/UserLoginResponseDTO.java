package com.flowtech.eventhub_spring_boot_backend.dto;

import lombok.Data;

@Data
public class UserLoginResponseDTO {

	private String email;
	private String roleName;
	private String token;
	
}
