package com.example.food.restaurant.service;

import java.util.List;

import org.springframework.stereotype.Service;
import com.example.food.restaurant.dto.RestaurantRequest;
import com.example.food.restaurant.dto.RestaurantResponse;
import com.example.food.restaurant.dto.UpdateRestaurantRequest;
import com.example.food.restaurant.entity.Restaurant;
import com.example.food.restaurant.mapper.RestaurantMapper;
import com.example.food.restaurant.repository.RestaurantRepository;
import com.example.food.user.entity.User;
import com.example.food.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantServiceImpl implements RestaurantService {

	
	private final RestaurantRepository restaurantRepository;
	private final UserRepository userRepository;
	private final RestaurantMapper restaurantMapper;

	@Override
	public RestaurantResponse createRestaurant(RestaurantRequest request, String ownerEmail) {
		User owner = userRepository.findByEmail(ownerEmail).orElseThrow(() -> new RuntimeException("Owner not found"));
		
		Restaurant restaurant = Restaurant.builder().name(request.getName()).description(request.getDescription()).address(request.getAddress())
		.city(request.getCity()).phoneNumber(request.getPhoneNumber()).isActive(true).owner(owner).build();
		
		Restaurant savedRestaurant = restaurantRepository.save(restaurant);
		
		return restaurantMapper.toResponse(savedRestaurant);
	}

	@Override
	public RestaurantResponse getRestaurantById(Long id) {
		Restaurant restaurant = restaurantRepository.findByIdWithOwner(id).orElseThrow(() -> new RuntimeException("Restaurant not found"));
		
		return restaurantMapper.toResponse(restaurant);
	}

	@Override
	public List<RestaurantResponse> getAllRestaurants() {
		
		return restaurantRepository.findByIsActiveTrue().stream().map(restaurantMapper::toResponse).toList();
	}

	@Override
	public List<RestaurantResponse> getMyRestaurants(String ownerEmail) {
		User owner = userRepository.findByEmail(ownerEmail).orElseThrow(() -> new RuntimeException("Owner not found"));
		
		return restaurantRepository.findByOwnerId(owner.getId()).stream().map(restaurantMapper::toResponse).toList();
	}

	@Override
	public RestaurantResponse updateRestaurant(Long restaurantId, UpdateRestaurantRequest request) {
		
		Restaurant restaurant = restaurantRepository.findByIdWithOwner(restaurantId).orElseThrow(() -> new RuntimeException("Restaurant not found"));
		
		restaurant.setName(request.getName());
		restaurant.setDescription(request.getDescription());
		restaurant.setAddress(request.getAddress());
		restaurant.setCity(request.getCity());
		restaurant.setPhoneNumber(request.getPhoneNumber());
		
		Restaurant updatedRestaurant = restaurantRepository.save(restaurant);
		
		return restaurantMapper.toResponse(updatedRestaurant);
		
	}

	@Override
	public void deleteRestaurant(Long restaurantId) {
		 Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new RuntimeException("No restaurant by that id"));
		 
		 restaurantRepository.delete(restaurant);
		
	}

}
