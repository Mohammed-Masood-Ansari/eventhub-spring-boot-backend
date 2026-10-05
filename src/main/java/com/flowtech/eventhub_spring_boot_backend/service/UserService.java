package com.flowtech.eventhub_spring_boot_backend.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.flowtech.eventhub_spring_boot_backend.dto.UserRegisterRequestDTO;
import com.flowtech.eventhub_spring_boot_backend.entity.Role;
import com.flowtech.eventhub_spring_boot_backend.entity.User;
import com.flowtech.eventhub_spring_boot_backend.mapper.UserMapper;
import com.flowtech.eventhub_spring_boot_backend.repository.RoleRepository;
import com.flowtech.eventhub_spring_boot_backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository; 
	private final UserMapper userMapper;
	private final PasswordEncoder passwordEncoder;
	
	public ResponseEntity<?> registerUser(@RequestBody UserRegisterRequestDTO requestDTO) {

		Role role = roleRepository.findByName(requestDTO.getRoleName())
				.orElseThrow(() -> new RuntimeException("Role not found"));

		User user = userMapper.toUser(requestDTO);

		user.setRole(role);

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		if(userRepository.existsByEmail(user.getEmail())|| userRepository.existsByPhone(user.getPhone())) {
			return ResponseEntity.badRequest().body("Email already exists or Phone number already exists");
		}
		
		User savedUser = userRepository.save(user);

		return savedUser != null ? ResponseEntity.ok("User registered successfully")
				: ResponseEntity.badRequest().body("User registration failed");
	}
	
	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email).orElseThrow(()-> new RuntimeException("User not found with email: " + email));
	}
}
