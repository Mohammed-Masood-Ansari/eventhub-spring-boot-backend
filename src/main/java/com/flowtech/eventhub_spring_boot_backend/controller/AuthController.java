package com.flowtech.eventhub_spring_boot_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flowtech.eventhub_spring_boot_backend.dto.UserRegisterRequestDTO;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {

	@PostMapping(value = "/register")
	public ResponseEntity<?> registerUser(@RequestBody UserRegisterRequestDTO requestDTO){
		return null;
	}
}
