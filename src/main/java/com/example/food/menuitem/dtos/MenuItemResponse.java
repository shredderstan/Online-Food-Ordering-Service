package com.example.food.menuitem.dtos;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MenuItemResponse {
	
	private Long id;
	private String name;
	private String description;
	private BigDecimal price;
	private String imageUrl;
	private String category;
	private Boolean available;
	private Long menuId;
	private String menuName;
}
