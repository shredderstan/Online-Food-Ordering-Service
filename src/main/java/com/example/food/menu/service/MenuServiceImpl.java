package com.example.food.menu.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.food.exception.ResourceNotFoundException;
import com.example.food.menu.dto.MenuRequest;
import com.example.food.menu.dto.MenuResponse;
import com.example.food.menu.entity.Menu;
import com.example.food.menu.mapper.MenuMapper;
import com.example.food.menu.repository.MenuRepository;
import com.example.food.restaurant.entity.Restaurant;
import com.example.food.restaurant.repository.RestaurantRepository;
import com.example.food.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

	private final RestaurantRepository restaurantRepo;
	private final MenuRepository menuRepo;
	private final MenuMapper menuMapper;
	
	@Override
	public MenuResponse createMenu(Long restaurantId, MenuRequest request) {
		Restaurant restaurant = restaurantRepo.findById(restaurantId).orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));
		Menu menu = Menu.builder().name(request.getName()).description(request.getDescription()).active(true).restaurant(restaurant).build();
		Menu savedMenu = menuRepo.save(menu);
		
		return menuMapper.toResponse(savedMenu);
		
	}

	@Override
	public List<MenuResponse> getMenusByRestaurant(Long restaurantId) {

		return menuRepo.findByRestaurantIdAndActiveTrue(restaurantId).stream()
				.map(menuMapper::toResponse).toList();
		
	}

	@Override
	public MenuResponse getMenuById(Long menuId) {
		Menu menu = menuRepo.findById(menuId).orElseThrow(() -> new ResourceNotFoundException("Menu not found"));
		
		return menuMapper.toResponse(menu);
	}

	@Override
	public MenuResponse updateMenu(Long menuId, MenuRequest request) {
		Menu menu = menuRepo.findById(menuId).orElseThrow(() -> new ResourceNotFoundException("Menu not found"));
		
		menu.setName(request.getName());
		menu.setDescription(request.getDescription());
		Menu savedMenu = menuRepo.save(menu);
		
		return menuMapper.toResponse(savedMenu);
		
	}

	@Override
	public void deleteMenu(Long id) {
		Menu menu =menuRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Menu not found"));
		menuRepo.delete(menu);
		
	}

}
