package com.example.food.restaurant.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.food.restaurant.dto.RestaurantRequest;
import com.example.food.restaurant.dto.RestaurantResponse;
import com.example.food.restaurant.dto.UpdateRestaurantRequest;
import com.example.food.restaurant.service.RestaurantService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {
	
	private final RestaurantService restaurantService;
	
	@GetMapping
	public ResponseEntity<?> getAllRestaurants() {
		return ResponseEntity.ok(restaurantService.getAllRestaurants());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getRestaurantById(@PathVariable Long id) {
		return ResponseEntity.ok(restaurantService.getRestaurantById(id));
	}
	
	@PreAuthorize("hasAnyRole('RESTAURANT_OWNER', 'ADMIN')")
	@PostMapping
	public ResponseEntity<?> createRestaurant(@Valid @RequestBody RestaurantRequest request, Authentication authentication) {
		String ownerEmail = authentication.getName();
		
		RestaurantResponse restaurant = restaurantService.createRestaurant(request, ownerEmail);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(restaurant);
	}
	
	@PreAuthorize("hasAnyRole('RESTAURANT_OWNER', 'ADMIN')")
	@GetMapping("/my")
	public ResponseEntity<?> getMyRestaurants(Authentication authentication) {
		String ownerEmail = authentication.getName();
		
		return ResponseEntity.ok(restaurantService.getMyRestaurants(ownerEmail));
	}
	
	@PreAuthorize("hasRole('ADMIN') or " + "@restaurantSecurity.isOwner(authentication, #id)")
	@PutMapping("/{id}")
	public ResponseEntity<?> updateRestaurant(@PathVariable Long id, @Valid @RequestBody UpdateRestaurantRequest request) {
		return ResponseEntity.ok(restaurantService.updateRestaurant(id, request));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteRestaurant(@PathVariable Long id) {
		restaurantService.deleteRestaurant(id);
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}
