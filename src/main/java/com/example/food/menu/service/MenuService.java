package com.example.food.menu.service;

import java.util.List;

import com.example.food.menu.dto.MenuRequest;
import com.example.food.menu.dto.MenuResponse;

public interface MenuService {
	
	MenuResponse createMenu(Long restaurantId, MenuRequest request);
	List<MenuResponse> getMenusByRestaurant(Long restaurantId);
	MenuResponse getMenuById(Long menuId);
	MenuResponse updateMenu(Long menuId, MenuRequest request);
	void deleteMenu(Long id);
}
