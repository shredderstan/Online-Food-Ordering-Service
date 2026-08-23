package com.example.food.menu.security;

import org.springframework.security.core.Authentication;

import com.example.food.menu.repository.MenuRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MenuSecurity {
	
	private final MenuRepository menuRepository;

	public boolean isOwner(Authentication authentication, Long menuId) {

	    if (authentication == null || !authentication.isAuthenticated()) 
	        return false;

	    return menuRepository.isOwnedByUser(menuId, authentication.getName());
	}
}
