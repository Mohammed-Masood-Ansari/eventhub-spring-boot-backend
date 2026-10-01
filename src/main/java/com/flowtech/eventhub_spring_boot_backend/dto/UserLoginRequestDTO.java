package com.flowtech.eventhub_spring_boot_backend.dto;

import lombok.Data;

@Data
public class UserLoginRequestDTO {

	private String email;
	private String password;
}
