package com.example.scm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.scm.beans.PurchaseOrderBo;
import com.example.scm.beans.UserLogin;
import com.example.scm.exception.ApiResponse;
import com.example.scm.service.PurchseOrderService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class PurchaseOrderController {

	@Autowired
	private PurchseOrderService service;
	
	
	
	@PostMapping("/savePoData")
	public ResponseEntity<ApiResponse<?>> savePoData(@RequestBody PurchaseOrderBo bo){
		
		
		log.info("PurchaseOrderController:savePoData Request body {}");
 			String response=service.savePoData(bo);
			log.info("");
			ApiResponse<?> apiResponse=ApiResponse.builder().
					status("Success").
					results(response).
					build();
			log.info("PurchaseOrderController:savePoData response");
			
			return new ResponseEntity<ApiResponse<?>>(apiResponse,HttpStatus.CREATED);
			
	}	
	
	
	
	
	@PostMapping("/validateSendOtp")
	public ResponseEntity<ApiResponse<?>> validateCredentials(@RequestParam String username,@RequestParam  String password,@RequestParam String phonenumber  ) throws Exception{
		
		
		log.info("PurchaseOrderController:validateCredentials Request Param {}");
 			boolean response=service.validateCredentials(username, password, phonenumber);
			log.info("");
			ApiResponse<?> apiResponse=ApiResponse.builder().
					status("Success").
					results(response).
					build();
			log.info("PurchaseOrderController:validateCredentials response");
			
			return new ResponseEntity<ApiResponse<?>>(apiResponse,HttpStatus.CREATED);
			
	}	
	
	@PostMapping("/saveUser")
	public ResponseEntity<ApiResponse<?>> saveUser(@RequestBody UserLogin user){
		
		log.info("PurchaseOrderController:validateCredentials Request Param {}");
		   String respone= service.saveCredentials(user);
		   ApiResponse<?> apiResponse=ApiResponse.builder().
				                                   status("Success").
				                                   results(respone).
				                                   build();
		    
		return new ResponseEntity<ApiResponse<?>>(apiResponse,HttpStatus.CREATED);
		
	}
	
	
	    }
		
	    
		
	
		
	
	

