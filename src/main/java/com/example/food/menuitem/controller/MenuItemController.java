package com.example.food.menuitem.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.food.menuitem.dtos.MenuItemRequest;
import com.example.food.menuitem.dtos.MenuItemResponse;
import com.example.food.menuitem.service.MenuItemService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MenuItemController {

	private final MenuItemService menuItemService;
	
	@GetMapping("/api/menus/{menuId}/items")
	public ResponseEntity<?> getItemsByMenu(@PathVariable Long menuId) {
		return ResponseEntity.ok(menuItemService.getItemsByMenu(menuId));
	}
	
	@PreAuthorize(
			"hasRole('ADMIN') or " + "(hasRole('RESTAURANT_OWNER') and " + 
	"@menuSecurity.isOwner(authentication, #menuId))")
	@PostMapping("/api/menus/{menuId}/items")
	public ResponseEntity<?> createItem(@PathVariable Long menuId, @Valid @RequestBody MenuItemRequest request) {
		MenuItemResponse item = menuItemService.createItem(menuId, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(item);
	}
	
	@GetMapping("/api/menu-items/{id}")
	public ResponseEntity<?> getItemById(@PathVariable Long id) {
		return ResponseEntity.ok(menuItemService.getItemById(id));
	}
	
	@PreAuthorize("hasRole('ADMIN') or " + "(hasRole('RESTAURANT_OWNER') and " + 
	"@menuItemSecurity.isOwner(authentication, #id))")
	@PutMapping("/api/menu-items/{id}")
	public ResponseEntity<?> updateItem(@PathVariable Long id, @Valid @RequestBody MenuItemRequest request) {
		return ResponseEntity.ok(menuItemService.updateItem(id, request));
	}
	
	@PreAuthorize("hasRole('ADMIN') or " + "(hasRole('RESTAURANT_OWNER') and " + 
	"@menuItemSecurity.isOwner(authentication, #id))")
	@DeleteMapping("/api/menu-items/{id}")
	public ResponseEntity<?> deleteItem(@PathVariable Long id) {
		menuItemService.deleteItem(id);
		return ResponseEntity.noContent().build();
	}
	
	@PreAuthorize("hasRole('ADMIN') or " + "(hasRole('RESTAURANT_OWNER') and" + 
	"@menuItemSecurity.isOwner(authentication, #id))")
	@PatchMapping("/api/menu-items/{id}/availability")
	public ResponseEntity<?> updateAvailability(@PathVariable Long id, @RequestParam boolean available) {
		return ResponseEntity.ok(menuItemService.updateAvailability(id, available));
	}
	
	
}
