package com.flowtech.eventhub_spring_boot_backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.flowtech.eventhub_spring_boot_backend.dto.UserRegisterRequestDTO;
import com.flowtech.eventhub_spring_boot_backend.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

	@Mapping(target = "role", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "id", ignore = true)
	public User toUser(UserRegisterRequestDTO userRegisterRequestDTO);
}
