package com.example.food.menuitem.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.food.menuitem.dtos.MenuItemResponse;
import com.example.food.menuitem.entity.MenuItem;

@Mapper(componentModel = "spring")
public interface MenuItemMapper {
	@Mapping(target = "menuId", source = "menu.id")
	@Mapping(target = "menuName", source = "menu.name")
	MenuItemResponse toResponse(MenuItem menuItem);
}
