package com.example.food.restaurant.service;

import java.util.List;

import com.example.food.restaurant.dto.RestaurantRequest;
import com.example.food.restaurant.dto.RestaurantResponse;
import com.example.food.restaurant.dto.UpdateRestaurantRequest;

public interface RestaurantService {

	RestaurantResponse createRestaurant(RestaurantRequest request, String ownerEmail);
	RestaurantResponse getRestaurantById(Long id);
	List<RestaurantResponse> getAllRestaurants();
	List<RestaurantResponse> getMyRestaurants(String ownerEmail);
	RestaurantResponse updateRestaurant(Long restaurantId, UpdateRestaurantRequest request);
	void deleteRestaurant(Long restaurantId);
	
}
