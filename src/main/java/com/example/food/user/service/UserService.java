package com.example.food.user.service;

import java.util.List;

import com.example.food.user.dto.LoginRequest;
import com.example.food.user.dto.LoginResponse;
import com.example.food.user.dto.UpdateUserRequest;
import com.example.food.user.dto.UserRequest;
import com.example.food.user.dto.UserResponse;

import jakarta.validation.Valid;

public interface UserService {
	
	UserResponse createUser(UserRequest request);
	UserResponse getUserById(Long id);
	UserResponse getUserByEmail(String email);
	List<UserResponse> getAllUsers();
	UserResponse updateUser(Long id, UpdateUserRequest request);
	void deleteUser(Long id);
	//UserResponse login(LoginRequest request);
	LoginResponse login(LoginRequest request);
	UserResponse updateUserByEmail(String email, @Valid UpdateUserRequest request);

}
