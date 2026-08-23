package com.example.food.user.dto;

import java.time.LocalDateTime;

import com.example.food.user.entity.UserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserResponse {
	private Long id;
	
	private String firstName;
	
	private String lastName;
	
	private String email;
	
	private String phoneNumber;
	
	private UserRole role;
	
	private LocalDateTime createdAt;
	
	private LocalDateTime updatedAt;
}
