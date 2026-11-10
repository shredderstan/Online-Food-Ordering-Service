package com.example.food.order.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.food.order.dto.CheckoutRequest;
import com.example.food.order.dto.OrderResponse;
import com.example.food.order.dto.UpdateOrderStatusRequest;
import com.example.food.order.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;

	@PostMapping("/checkout")
	public ResponseEntity<OrderResponse> checkout(
			Authentication authentication,
			@Valid @RequestBody CheckoutRequest request) {
		OrderResponse response = orderService.checkout(authentication.getName(), request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/my")
	public ResponseEntity<List<OrderResponse>> getMyOrders(Authentication authentication) {
		return ResponseEntity.ok(orderService.getMyOrders(authentication.getName()));
	}

	@PreAuthorize("@orderSecurity.canAccessOrder(authentication, #id)")
	@GetMapping("/{id}")
	public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
		return ResponseEntity.ok(orderService.getOrderById(id));
	}

	@PreAuthorize("@orderSecurity.isCustomerOwner(authentication, #id)")
	@PostMapping("/{id}/cancel")
	public ResponseEntity<OrderResponse> cancelOrder(
			Authentication authentication,
			@PathVariable Long id) {
		return ResponseEntity.ok(orderService.cancelOrder(id, authentication.getName()));
	}

	@PreAuthorize("hasRole('ADMIN') or (hasRole('RESTAURANT_OWNER') and @restaurantSecurity.isOwner(authentication, #restaurantId))")
	@GetMapping("/restaurant/{restaurantId}")
	public ResponseEntity<List<OrderResponse>> getOrdersByRestaurant(@PathVariable Long restaurantId) {
		return ResponseEntity.ok(orderService.getOrdersByRestaurant(restaurantId));
	}

	@PreAuthorize("hasRole('ADMIN') or (hasRole('RESTAURANT_OWNER') and @orderSecurity.isRestaurantOwnerForOrder(authentication, #id))")
	@PatchMapping("/{id}/status")
	public ResponseEntity<OrderResponse> updateOrderStatus(
			@PathVariable Long id,
			@Valid @RequestBody UpdateOrderStatusRequest request) {
		return ResponseEntity.ok(orderService.updateOrderStatus(id, request));
	}
}

