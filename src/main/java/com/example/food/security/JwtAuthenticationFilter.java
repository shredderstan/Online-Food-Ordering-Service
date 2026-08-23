package com.example.food.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
    		throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // System.out.println("Authorization Header: " + authHeader);

        // No JWT provided
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

            System.out.println("No Bearer token found");

            filterChain.doFilter(request, response);

            return;
        }

        String jwt = authHeader.substring(7);

        // System.out.println("JWT received: " + jwt);

        String email;

        try {

            email = jwtService.extractUsername(jwt);

            // System.out.println("Email extracted from JWT: " + email);

        } 
        
        catch (Exception e) {

            // System.out.println("Failed to extract username from JWT");

            e.printStackTrace();

            filterChain.doFilter(request, response);

            return;
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails;

            try {

                userDetails = userDetailsService.loadUserByUsername(email);

//                System.out.println("User found: " + userDetails.getUsername());
//
//                System.out.println("Authorities: "+ userDetails.getAuthorities());

            } 
            
            catch (Exception e) {

                //System.out.println("Failed to load user");

                e.printStackTrace();

                filterChain.doFilter(request, response);

                return;
            }

            boolean validToken;

            try {

                validToken = jwtService.isTokenValid(jwt);

                // System.out.println("JWT valid: "+ validToken);

            } catch (Exception e) {

                // System.out.println("JWT validation failed");

                e.printStackTrace();

                validToken = false;
            }

            if (validToken) {

                UsernamePasswordAuthenticationToken authentication = 
                		new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);

                // System.out.println("Authentication successfully set");
            }
        }

        filterChain.doFilter(request,response);
    }
}