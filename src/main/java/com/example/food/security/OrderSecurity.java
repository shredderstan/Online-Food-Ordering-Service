package com.example.food.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.example.food.order.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Component("orderSecurity")
@RequiredArgsConstructor
public class OrderSecurity {

	private final OrderRepository orderRepository;

	public boolean isCustomerOwner(Authentication authentication, Long orderId) {
		if (authentication == null || !authentication.isAuthenticated()) {
			return false;
		}
		return orderRepository.isCustomerOwner(orderId, authentication.getName());
	}

	public boolean isRestaurantOwnerForOrder(Authentication authentication, Long orderId) {
		if (authentication == null || !authentication.isAuthenticated()) {
			return false;
		}
		return orderRepository.isRestaurantOwnerForOrder(orderId, authentication.getName());
	}

	public boolean canAccessOrder(Authentication authentication, Long orderId) {
		if (authentication == null || !authentication.isAuthenticated()) {
			return false;
		}
		if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
			return true;
		}
		String email = authentication.getName();
		return orderRepository.isCustomerOwner(orderId, email) || orderRepository.isRestaurantOwnerForOrder(orderId, email);
	}
}

