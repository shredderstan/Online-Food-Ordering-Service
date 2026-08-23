package com.example.food.user.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.food.user.dto.LoginRequest;
import com.example.food.user.dto.UpdateUserRequest;
import com.example.food.user.dto.UserRequest;
import com.example.food.user.dto.UserResponse;
import com.example.food.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
	
	private final UserService userService;
	
	
	// ADMIN only
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping
	public ResponseEntity<?> getAllUsers() {
		return ResponseEntity.ok(userService.getAllUsers());
	}
	
	// ANY AUTHENTICATED USER
	@GetMapping("/me")
	public ResponseEntity<?> getCurrentUser(Authentication authentication) {
		
		String email = authentication.getName();
		
		return ResponseEntity.ok(userService.getUserByEmail(email));
	}
	
	// ANY AUTHENTICATED USER - OWN ACCOUNT ONLY
	@PutMapping("/me")
	public ResponseEntity<?> updateCurrentUser(Authentication authentication, @Valid @RequestBody UpdateUserRequest request) {
		
		String email = authentication.getName();
		
		return ResponseEntity.ok(userService.updateUserByEmail(email, request));
	}
	
	// ADMIN OR OWNER
	@PreAuthorize("hasRole('ADMIN') or @userSecurity.isOwner(authentication, #id)")
	@GetMapping("/{id}")
	public ResponseEntity<?> getUserById(@PathVariable Long id) {
		return ResponseEntity.ok(userService.getUserById(id));
	}
	
	// PUBLIC
	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
		return ResponseEntity.ok(userService.login(request));
	}
	
	// PUBLIC
	@PostMapping("/register")
	public ResponseEntity<?> createUser(@Valid @RequestBody UserRequest request) {
		UserResponse user = userService.createUser(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(user);
	}
	
	// ADMIN ONLY
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id}")
	public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest request) {
		UserResponse user = userService.updateUser(id, request);
		return ResponseEntity.ok(user);
	}
	
	// ADMIN ONLY
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable Long id) {
		userService.deleteUser(id);
		return ResponseEntity.noContent().build();
	}
	
	
}
