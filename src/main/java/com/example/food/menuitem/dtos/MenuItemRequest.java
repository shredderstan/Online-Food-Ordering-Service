package com.example.food.menuitem.dtos;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MenuItemRequest {

	@NotBlank(message = "Item name is required")
	@Size(max = 100)
	private String name;
	
	@Size(max = 500)
	private String description;
	
	@NotNull(message = "Price is required")
	@DecimalMin(value = "0.01", message = "Price must be greater than 0")
	private BigDecimal price;
	
	private String imageUrl;
	
	private String category;
	
}
