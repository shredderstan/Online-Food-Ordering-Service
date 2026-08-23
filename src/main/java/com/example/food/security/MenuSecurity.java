package com.example.food.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.example.food.menu.repository.MenuRepository;

import lombok.RequiredArgsConstructor;

@Component("menuSecurity")
@RequiredArgsConstructor
public class MenuSecurity {

	private final MenuRepository menuRepo;
	
	public boolean isOwner(Authentication authentication, Long menuId) {
		
		if(authentication == null || !authentication.isAuthenticated())
			return false;
		
		return menuRepo.isOwnedByUser(menuId, authentication.getName());
	}
}
