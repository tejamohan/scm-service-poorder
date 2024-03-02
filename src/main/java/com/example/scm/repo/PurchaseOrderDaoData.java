package com.example.scm.repo;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;

import com.example.scm.beans.PurchaseOrderDao;
import com.example.scm.beans.UserLogin;

public interface PurchaseOrderDaoData {

	public Long savePo(PurchaseOrderDao dao);

	public String updatePoDetails(PurchaseOrderDao dao);
	
	 
	public boolean validateCredentials(String username, String password);
	public boolean verifyOTP(String username, String otp);
	public boolean updateOTP(String otp,String username);
	public String insertUser(UserLogin user);
	



}
