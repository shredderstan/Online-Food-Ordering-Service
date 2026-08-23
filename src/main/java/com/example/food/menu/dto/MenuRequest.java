package com.example.food.menu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MenuRequest {
	
	@NotBlank(message = "Menu name is required")
	@Size(max = 100)
	private String name;
	
	@Size(max = 500)
	private String description;

}
