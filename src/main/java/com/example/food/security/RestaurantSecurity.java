package com.example.food.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.example.food.restaurant.repository.RestaurantRepository;

import lombok.RequiredArgsConstructor;

@Component("restaurantSecurity")
@RequiredArgsConstructor
public class RestaurantSecurity {
	
	private final RestaurantRepository restaurantRepo;
	
	
	public boolean isOwner(Authentication authentication, Long restaurantId) {
		
		if(authentication == null || !authentication.isAuthenticated()) {
			return false;
		}
		
		String email = authentication.getName();
		
		return restaurantRepo.existsByIdAndOwnerEmailIgnoreCase(restaurantId, email);
			
	}
}
