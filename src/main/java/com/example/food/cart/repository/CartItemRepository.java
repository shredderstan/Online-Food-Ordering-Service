package com.example.food.cart.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.food.cart.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
	
	Optional<CartItem> findByCartIdAndMenuItemId(Long cartId, Long menuItemId);
	
	Optional<CartItem> findByIdAndCartId(Long id, Long cartId);

}
