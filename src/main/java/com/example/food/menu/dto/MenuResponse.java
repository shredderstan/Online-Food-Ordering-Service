package com.example.food.menu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MenuResponse {

	private Long id;
	private String name;
	private String description;
	private Boolean active;
	private Long restaurantId;
	private String restaurantName;
	
}
