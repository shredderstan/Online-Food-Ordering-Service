package com.example.food.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantResponse {
	
	private Long id;
	
	private String name;
	
	private String description;
	
	private String address;
	
	private String city;
	
	private String phoneNumber;
	
	private boolean isActive;
	
	private Long ownerId;
	
	private String ownerName;
	
	
}
