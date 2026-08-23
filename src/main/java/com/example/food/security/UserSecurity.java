package com.example.food.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.example.food.user.entity.User;
import com.example.food.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component("userSecurity")
@RequiredArgsConstructor
public class UserSecurity {

	private final UserRepository userRepository;
	
	public boolean isOwner(Authentication authentication, Long userId) {
		
		if(authentication == null || !authentication.isAuthenticated()) {
			return false;
		}
		
		String email = authentication.getName();
		
		return userRepository.findById(userId).map(User::getEmail).map(userEmail -> userEmail.equalsIgnoreCase(email)).orElse(false);
	}
}
