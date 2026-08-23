package com.example.food.restaurant.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.food.restaurant.dto.RestaurantResponse;
import com.example.food.restaurant.entity.Restaurant;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {
	
	@Mapping(target = "ownerId", source = "owner.id")
	@Mapping(target = "ownerName", expression = "java(restaurant.getOwner().getFirstName() + \" \" + restaurant.getOwner().getLastName())")
	RestaurantResponse toResponse(Restaurant restaurant);

}
