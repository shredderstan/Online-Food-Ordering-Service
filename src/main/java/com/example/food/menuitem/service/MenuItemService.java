package com.example.food.menuitem.service;

import org.springframework.stereotype.Service;

import com.example.food.exception.ResourceNotFoundException;
import com.example.food.menu.entity.Menu;
import com.example.food.menu.repository.MenuRepository;
import com.example.food.menuitem.dtos.MenuItemRequest;
import com.example.food.menuitem.dtos.MenuItemResponse;
import com.example.food.menuitem.entity.MenuItem;
import com.example.food.menuitem.mapper.MenuItemMapper;
import com.example.food.menuitem.repository.MenuItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MenuItemService {

	
	private final MenuItemRepository menuItemRepository;
	private final MenuRepository menuRepository;
	private final MenuItemMapper menuItemMapper;
	
	public MenuItemResponse createItem(Long menuId, MenuItemRequest request) {
		Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new ResourceNotFoundException("Menu not found"));
		
		MenuItem item = MenuItem.builder().name(request.getName()).description(request.getDescription()).price(request.getPrice()).
		imageUrl(request.getImageUrl()).category(request.getCategory()).available(true).menu(menu).build();
		
		MenuItem saved = menuItemRepository.save(item);
		
		return menuItemMapper.toResponse(saved);
	}
	
	public MenuItemResponse getItemsByMenu(Long itemId) {
		MenuItem item = menuItemRepository.findByIdWithMenu(itemId).orElseThrow(() -> new ResourceNotFoundException("Menu Item not found"));
		
		return menuItemMapper.toResponse(item);
	}
	
	public MenuItemResponse updateItem(Long itemId, MenuItemRequest request) {
		MenuItem item = menuItemRepository.findByIdWithMenu(itemId).orElseThrow(() -> new ResourceNotFoundException("Menu Item not found"));
		
		item.setName(request.getName());
		item.setDescription(request.getDescription());
		item.setPrice(request.getPrice());
		item.setImageUrl(request.getImageUrl());
		item.setCategory(request.getCategory());
		
		MenuItem updated = menuItemRepository.save(item);
		
		return menuItemMapper.toResponse(updated);
	}
	
	public void deleteItem(Long itemId) {
		MenuItem item = menuItemRepository.findById(itemId).orElseThrow(() -> new ResourceNotFoundException("Menu Item not found"));
		
		menuItemRepository.delete(item);
	}
	
	public MenuItemResponse updateAvailability(Long itemId, boolean available) {
		MenuItem menuItem = menuItemRepository.findById(itemId).orElseThrow(() -> new ResourceNotFoundException("Menu Item not found"));
		
		menuItem.setAvailable(available);
		
		return menuItemMapper.toResponse(menuItem);
	}

	public MenuItemResponse getItemById(Long id) {
		MenuItem menu = menuItemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Menu Item not found"));
		
		return menuItemMapper.toResponse(menu);
	}

}
