package com.example.food.cart.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.food.cart.dto.AddCartItemRequest;
import com.example.food.cart.dto.CartItemResponse;
import com.example.food.cart.dto.CartResponse;
import com.example.food.cart.dto.UpdateCartItemRequest;
import com.example.food.cart.entity.Cart;
import com.example.food.cart.entity.CartItem;
import com.example.food.cart.repository.CartItemRepository;
import com.example.food.cart.repository.CartRepository;
import com.example.food.exception.ResourceNotFoundException;
import com.example.food.menuitem.entity.MenuItem;
import com.example.food.menuitem.repository.MenuItemRepository;
import com.example.food.user.entity.User;
import com.example.food.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {
	
	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	private final MenuItemRepository menuItemRepository;
	private final UserRepository userRepository;
	
	private CartResponse toResponse(Cart cart) {
		
		List<CartItemResponse> items = cart.getItems().stream().
				map(item -> CartItemResponse.builder().id(item.getId()).menuItemId(item.getMenuItem().getId())
						.menuItemName(item.getMenuItem().getName()).unitPrice(item.getUnitPrice()).quantity(item.getQuantity())
						.subTotal(item.getSubTotal()).build()).toList();
		
		return CartResponse.builder().id(cart.getId()).restaurantId(cart.getRestaurantId())
				.totalAmount(cart.getTotalAmount()).items(items).build();
	}
	
	private void recalculateCartTotal(Cart cart) {

		BigDecimal total = cart.getItems().stream().map(CartItem::getSubTotal).reduce(BigDecimal.ZERO, BigDecimal::add);

		cart.setTotalAmount(total);
	}
	
	public CartResponse getCart(String email) {
		 User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException(email + " does not exist"));
		 
		 return cartRepository.findByUserId(user.getId()).map(this::toResponse)
				 .orElse(CartResponse.builder().restaurantId(null).totalAmount(BigDecimal.ZERO).items(List.of()).build());
		 
	}
	
	public CartResponse addItem(String email, AddCartItemRequest request) {

		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException(email + " user not found"));

		MenuItem menuItem = menuItemRepository.findByIdWithMenu(request.getMenuItemId())
				.orElseThrow(() -> new ResourceNotFoundException("Menu item not found"));

		if (!menuItem.getAvailable()) {
			throw new RuntimeException("Menu item is not available");
		}

		Long restaurantId = menuItem.getMenu().getRestaurant().getId();

		Cart cart = cartRepository.findByUserId(user.getId()).orElse(null);

		// Create a new cart
		if (cart == null) {

			cart = Cart.builder().user(user).restaurantId(restaurantId).totalAmount(BigDecimal.ZERO)
					.items(new ArrayList<>()).build();

			cart = cartRepository.save(cart);
		}

		// Cart can only contain items from one restaurant
		if (!cart.getRestaurantId().equals(restaurantId)) {

			throw new RuntimeException("Cart can contain items from only one restaurant");
		}

		CartItem cartItem = cartItemRepository.findByCartIdAndMenuItemId(cart.getId(), menuItem.getId()).orElse(null);

		// New item
		if (cartItem == null) {

			BigDecimal subTotal = menuItem.getPrice().multiply(BigDecimal.valueOf(request.getQuantity()));

			cartItem = CartItem.builder().cart(cart).menuItem(menuItem).quantity(request.getQuantity())
					.unitPrice(menuItem.getPrice()).subTotal(subTotal).build();

			cart.getItems().add(cartItem);

		}

		// Existing item
		else {

			int newQuantity = cartItem.getQuantity() + request.getQuantity();

			cartItem.setQuantity(newQuantity);

			cartItem.setSubTotal(cartItem.getUnitPrice().multiply(BigDecimal.valueOf(newQuantity)));
		}

		// Persist CartItem
		cartItemRepository.saveAndFlush(cartItem);

		/*
		 * IMPORTANT: Reload the Cart so that we have the actual persisted CartItems.
		 */
		cart = cartRepository.findById(cart.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

		// Calculate total from persisted items
		recalculateCartTotal(cart);

		cartRepository.save(cart);

		return toResponse(cart);
	}
	
	public CartResponse updateItem(String email, Long cartItemId, UpdateCartItemRequest request) {
		User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
		Cart cart = cartRepository.findByUserId(user.getId()).orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
		CartItem cartItem = cartItemRepository.findByIdAndCartId(cartItemId, cart.getId()).orElseThrow(() -> new ResourceNotFoundException("CartItem not found"));
		
		cartItem.setQuantity(request.getQuantity());
		cartItem.setSubTotal(cartItem.getUnitPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
		
		recalculateCartTotal(cart);
		
		return toResponse(cart);
	}
	
	public void removeItem(String email, Long cartItemId) {
		
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));
		
		Cart cart = cartRepository.findByUserId(user.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
		
		CartItem cartItem = cartItemRepository.findByIdAndCartId(cartItemId, cart.getId())
				.orElseThrow(() -> new ResourceNotFoundException("CartItem not found"));

		cartItemRepository.delete(cartItem);
		cart.getItems().removeIf(item -> item.getId().equals(cartItemId));
		recalculateCartTotal(cart);
	}
	
	public void clearCart(String email) {
		
		User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
		Cart cart = cartRepository.findByUserId(user.getId()).orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
		
		cart.getItems().clear();
		cart.setTotalAmount(BigDecimal.ZERO);
	}

}
