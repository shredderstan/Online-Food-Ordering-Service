package com.example.food.exception;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
	
	private LocalDateTime timeStamp;
	
	private int status;
	
	private String message;
	
	
}
