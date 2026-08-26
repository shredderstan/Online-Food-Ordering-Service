package com.example.food.cart.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.food.cart.dto.AddCartItemRequest;
import com.example.food.cart.dto.UpdateCartItemRequest;
import com.example.food.cart.service.CartService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
	
	private final CartService cartService;
	
	@GetMapping
	public ResponseEntity<?> getCart(Authentication authentication) {
		return ResponseEntity.ok(cartService.getCart(authentication.getName()));
	}

	@PostMapping("/items")
	public ResponseEntity<?> addItem(Authentication authentication, @Valid @RequestBody AddCartItemRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(cartService.addItem(authentication.getName(), request));
	}
	
	@PutMapping("/items/{itemId}")
	public ResponseEntity<?> updateItem(Authentication authentication, @PathVariable Long itemId, @Valid @RequestBody UpdateCartItemRequest request) {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(cartService.updateItem(authentication.getName(), itemId, request));
	}
	
	@DeleteMapping("/items/{itemId}")
	public ResponseEntity<?> removeItem(Authentication authentication, @PathVariable Long itemId) {
		cartService.removeItem(authentication.getName(), itemId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	@DeleteMapping
	public ResponseEntity<?> clearCart(Authentication authentication) {
		cartService.clearCart(authentication.getName());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
