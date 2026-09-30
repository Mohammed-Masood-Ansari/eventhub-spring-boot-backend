package com.flowtech.eventhub_spring_boot_backend.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import com.flowtech.eventhub_spring_boot_backend.controller.AuthController;
import com.flowtech.eventhub_spring_boot_backend.entity.Role;
import com.flowtech.eventhub_spring_boot_backend.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleDataInitializer implements CommandLineRunner {

	private final RoleRepository roleRepository;

	@Override
	public void run(String... args) throws Exception {
		
		List<String> list=new ArrayList<String>(Arrays.asList("ROLE_ADMIN","ROLE_CUSTOMER","ROLE_ORGANISER"));
		
		for (String string : list) {
			
			if(!(roleRepository.existsByName(string))) {
				
				Role role = new Role();
				role.setName(string);
				
				this.roleRepository.save(role);
			}
		}	
	}
}
