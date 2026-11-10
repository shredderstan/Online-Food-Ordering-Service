package com.example.food.menu.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.food.menu.dto.MenuRequest;
import com.example.food.menu.dto.MenuResponse;
import com.example.food.menu.service.MenuService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MenuController {

	private final MenuService menuService;
	
	@GetMapping("/api/restaurants/{restaurantId}/menus")
	public ResponseEntity<?> getMenusByRestaurant(@PathVariable Long restaurantId) {
		return ResponseEntity.ok(menuService.getMenusByRestaurant(restaurantId));
	}
	
	@PreAuthorize("hasAnyRole('ADMIN') or (hasRole('RESTAURANT_OWNER') and @restaurantSecurity.isOwner(authentication, #restaurantId))")
	@PostMapping("/api/restaurants/{restaurantId}/menus")
	public ResponseEntity<?> createMenu(@PathVariable Long restaurantId, @Valid @RequestBody MenuRequest request) {
		MenuResponse menu = menuService.createMenu(restaurantId, request);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(menu);
	}
	
	@PreAuthorize("hasRole('ADMIN') or (hasRole('RESTAURANT_OWNER') and @menuSecurity.isOwner(authentication, #id))")
	@GetMapping("/api/menus/{id}")
	public ResponseEntity<?> getMenuById(@PathVariable Long id) {
		return ResponseEntity.ok(menuService.getMenuById(id));
	}
	
	@PutMapping("/api/menus/{id}")
	public ResponseEntity<?> updateMenu(@PathVariable Long id, @Valid @RequestBody MenuRequest request) {
		return ResponseEntity.ok(menuService.updateMenu(id, request));
	}
	
	@PreAuthorize("hasRole('ADMIN') or (hasRole('RESTAURANT_OWNER') and @menuSecurity.isOwner(authentication, #id))")
	@DeleteMapping("/api/menus/{id}")
	public ResponseEntity<?> deleteMenu(@PathVariable Long id) {
		menuService.deleteMenu(id);
		return ResponseEntity.noContent().build();
	}
	
	
	
	
}
