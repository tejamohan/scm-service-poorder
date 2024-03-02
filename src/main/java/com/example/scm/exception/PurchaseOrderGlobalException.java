package com.example.scm.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PurchaseOrderGlobalException {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ApiResponse<?> handlePurchaseOrderNotFoundExceprion(MethodArgumentNotValidException exception){
		
		ApiResponse<?> serviceResponse=new ApiResponse<>();
		List<ErrorDTO> error=new ArrayList<>();
		exception.getBindingResult().getFieldErrors().forEach(errors->{
			ErrorDTO dto=new ErrorDTO(errors.getField(), errors.getDefaultMessage());
			error.add(dto);
		});
		serviceResponse.setStatus("Failed");
		serviceResponse.setErrors(error);
					
		return serviceResponse;
		
	}
	
	

}
