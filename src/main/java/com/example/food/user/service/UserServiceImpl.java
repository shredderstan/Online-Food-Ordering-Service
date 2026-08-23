package com.example.food.user.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.food.exception.ResourceNotFoundException;
import com.example.food.security.JwtService;
import com.example.food.user.dto.LoginRequest;
import com.example.food.user.dto.LoginResponse;
import com.example.food.user.dto.UpdateUserRequest;
import com.example.food.user.dto.UserRequest;
import com.example.food.user.dto.UserResponse;
import com.example.food.user.entity.User;
import com.example.food.user.entity.UserRole;
import com.example.food.user.mapper.UserMapper;
import com.example.food.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	
	private final UserRepository userRepository;
	private final UserMapper userMapper;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	

	@Override
	public UserResponse createUser(UserRequest request) {
		if(userRepository.existsByEmail(request.getEmail()))
			throw new IllegalArgumentException("User already exists with user " + request.getEmail());
		
		User user = userMapper.toEntity(request);
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setRole(UserRole.CUSTOMER);
		User savedUser = userRepository.save(user);
		return userMapper.toResponse(savedUser);
		
	}
	
	@Override
	public LoginResponse login(LoginRequest request) {
		User user =userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
		
		boolean passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPassword());
		
		if(!passwordMatches)
			throw new IllegalArgumentException("Invalid email or password");
		
		String token = jwtService.generateToken(user.getEmail(), user.getRole().name());
		
		return LoginResponse.builder().accessToken(token).tokenType("Bearer").user(userMapper.toResponse(user)).build();
	}

	@Override
	public UserResponse getUserById(Long id) {
		User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
		return userMapper.toResponse(user);
	}

	@Override
	public UserResponse getUserByEmail(String email) {
		User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
		
		return userMapper.toResponse(user);
	}
	
	@Override
	public List<UserResponse> getAllUsers() {
		return userRepository.findAll().stream().map(userMapper::toResponse).toList();
	}

	@Override
	public UserResponse updateUser(Long id, UpdateUserRequest request) {
		User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("user not found with id: " + id));
		
		userMapper.updateUserFromRequest(request, user);
		
		User savedUser =userRepository.save(user);
		return userMapper.toResponse(savedUser);
	}

	@Override
	public void deleteUser(Long id) {
		User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
		userRepository.delete(user);

	}

	@Override
	public UserResponse updateUserByEmail(String email, @Valid UpdateUserRequest request) {
		
		User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
		
		userMapper.updateUserFromRequest(request, user);
		
		User updatedUser = userRepository.save(user);
		
		return userMapper.toResponse(updatedUser);
	}

//	@Override
//	public UserResponse login(LoginRequest request) {
//		User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
//		boolean passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPassword());
//		
//		if(!passwordMatches)
//			throw new IllegalArgumentException("Invalid email or password");
//		
//		return userMapper.toResponse(user);
//	}

}
