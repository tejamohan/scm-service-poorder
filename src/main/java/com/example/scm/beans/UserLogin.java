package com.example.scm.beans;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserLogin {
	
	
	    private String username;
	    private String password;
	    private String phoneNumber;
	    private String otp;
	    private LocalDateTime otpGeneratedTime;

}
