package com.example.food.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.example.food.user.dto.UpdateUserRequest;
import com.example.food.user.dto.UserRequest;
import com.example.food.user.dto.UserResponse;
import com.example.food.user.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
	
	User toEntity(UserRequest request);		// Maps UserRequest -> User Entity
	UserResponse toResponse(User user);		// Maps User -> UserResponseDTO
	void updateUserFromRequest(UpdateUserRequest request, @MappingTarget User user);

}
