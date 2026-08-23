package com.example.food.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.example.food.config.JwtProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {
	
	private final JwtProperties jwtConfig;
	
	private SecretKey getSigningKey() {
		return Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes());
	}
	
	private Claims extractAllClaims(String token) {

		return Jwts.parser().verifyWith(getSigningKey()).build()
				.parseSignedClaims(token)
				.getPayload();
	}
	
	public String generateToken(String email, String role) {
		Date now = new Date();
		
		Date expiration = new Date(now.getTime() + jwtConfig.getExpiration());
		
		return Jwts.builder().subject(email).claim("role", role).issuedAt(now).expiration(expiration)
				.signWith(getSigningKey()).compact();
	}

	public String extractUsername(String token) {
		return extractAllClaims(token).getSubject();
	}
	
	public String extractRole(String token) {
		return extractAllClaims(token).get("role", String.class);
	}
	
	public boolean isTokenValid(String token) {
		
		try {
			
			Claims claims = extractAllClaims(token);
			return claims.getExpiration().after(new Date());
			
		} catch (Exception e) {
			return false;
		}
	}
	
	
}

