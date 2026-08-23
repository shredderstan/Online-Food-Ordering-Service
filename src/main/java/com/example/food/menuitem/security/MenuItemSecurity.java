package com.example.food.menuitem.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.example.food.menuitem.repository.MenuItemRepository;

import lombok.RequiredArgsConstructor;

@Component("menuItemSecurity")
@RequiredArgsConstructor
public class MenuItemSecurity {

	private final MenuItemRepository menuItemRepository;
	
	public boolean isOwner(Authentication authentication, Long itemId) {
		if (authentication == null || !authentication.isAuthenticated())
			return false;
			
		return menuItemRepository.isOwnedByUser(itemId, authentication.getName());
	}
}
