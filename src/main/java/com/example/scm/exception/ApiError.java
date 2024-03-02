package com.example.scm.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class ApiError {
	
	private String message;
	private HttpStatus status;
	private LocalDateTime times;

}
