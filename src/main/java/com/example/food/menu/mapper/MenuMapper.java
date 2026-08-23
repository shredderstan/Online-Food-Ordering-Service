package com.example.food.menu.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.food.menu.dto.MenuResponse;
import com.example.food.menu.entity.Menu;

@Mapper(componentModel = "spring")
public interface MenuMapper {

	@Mapping(target = "restaurantId", source = "restaurant.id")
	@Mapping(target = "restaurantName", source = "restaurant.name")
	MenuResponse toResponse(Menu menu);
}
