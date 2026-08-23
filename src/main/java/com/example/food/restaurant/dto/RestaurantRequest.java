package com.example.food.restaurant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RestaurantRequest {
	
	@NotBlank(message = "Restaurant name is required")
	@Size(max = 100)
	private String name;
	
	@NotBlank(message = "Description is required")
	@Size(max = 500)
	private String description;
	
	@NotBlank(message = "Address is required")
	private String address;
	
	@NotBlank(message = "City is required")
	private String city;
	
	@NotBlank(message = "Phone number is required")
	private String phoneNumber;
	
	

}
