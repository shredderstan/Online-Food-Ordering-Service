package com.example.food.cart.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.example.food.cart.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long>{
	
	//Optional<Cart> findByUserId(Long userId);
	
	@EntityGraph(attributePaths = {"items", "items.menuItem", "items.menuItem.menu"})
	Optional<Cart> findByUserId(@Param("userId") Long userId);
	
	
}
